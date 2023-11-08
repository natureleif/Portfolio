/*
 * Andrew Kuhlken
 * kuhlkena
 */

#include "cachelab.h"
#include <unistd.h>
#include <stdlib.h>
#include <stdio.h>
#include <getopt.h>
#include <string.h>
#include <math.h>

typedef unsigned long mem_addr_t;

/* forward */
void simulate_cache(int sval, int bval, int Eval, FILE *tracefile, int verbose);


int read_reference(FILE *infile,
		   char *reftype, mem_addr_t* addr, unsigned *length,
		   int verbose)
{
    /*
     * Read the next reference from trace file infile,
     * setting the other parameters:
     *   reftype is one of the characters I S L M
     *   addr is the starting address
     *   length is the length of the reference
     *
     * Returns non-zero if successful, 0 at end of file.
     */
    static int line_num = 0;
    char buf[1000];
    char* result;
    while ((result = fgets(buf, 1000, infile)) != NULL) {
        line_num += 1;
        char i_ref = buf[0];
        char ref = buf[1];
        if (ref == 'S' || ref == 'L' || ref == 'M') {
	    *reftype = ref;
        } else if (i_ref == 'I') {
	    *reftype = i_ref;
        } else {
	    if (verbose) {
	        printf("Ignoring line %d: '%s'\n", line_num, buf);
	    }
	    continue;
	}      
      
	int matched = sscanf(buf+3, "%lx,%u", addr, length);
        if (matched != 2) {
	    fprintf(stderr, "Malformed line number %d: '%s'\n",
		    line_num, buf);
            exit(1);
        }
	return 1;
    }
    return 0;
}

void usage(char *argv[]) {
    fprintf(stderr, 
	    "Usage: %s -s <s> -E <E> -b <b> -t <tracefile> [-v]\n",
	    argv[0]);
    exit(0);
}

int main(int argc, char *argv[])
{
    int opt;
    int bval = -1;
    int sval = -1;
    int Eval = -1;
    char *tracefilename = NULL;
    int verbose = 0; // default not verbose
    FILE *tracefile;
    while( (opt = getopt(argc, argv, "s:E:b:t:vh")) != -1) {
        switch(opt) {
            case 'b':
                bval = atoi(optarg);
                break;
            case 's':
                sval = atoi(optarg);
                break;
            case 'E':
                Eval = atoi(optarg);
                break;
            case 't':
                tracefilename = malloc(strlen(optarg) + 1);
                strncpy(tracefilename, optarg, strlen(optarg));
                break;
            case 'v':
                verbose += 1;
                break;
            case 'h':
            default:
                usage(argv);

        }
    }
    if(tracefilename == NULL) {
        fprintf(stderr, "Must specify a trace file\n");
        usage(argv);
    }
    if(sval == -1 || bval == -1 || Eval == -1) {
        fprintf(stderr, "Must specify all of -s, -b and -E\n");
        usage(argv);
    }
    if(verbose) {
        printf("s: %d b: %d E: %d t: %s\n", sval, bval, Eval, tracefilename);
    }

    tracefile = fopen(tracefilename, "r");
    if(tracefile == NULL) {
      fprintf(stderr, "Unable to open '%s':", tracefilename);
      perror(NULL);
      exit(1);
    }

    simulate_cache(sval, bval, Eval, tracefile, verbose);

    fclose(tracefile);
    free(tracefilename);
    return 0;
}


struct Block{
    int age;
    int valid;
    mem_addr_t tag;
};

// Retruns first non-valid or oldest valid block in set
struct Block* get_replace(struct Block* setptr, int Eval){
    struct Block* oldest;
    int maxage = -1;
    for(int i=0; i<Eval; i++){
        // Find best block to replace
        if(setptr[i].valid==0){
            return &setptr[i];
        } else if(setptr[i].age > maxage){
            oldest = &setptr[i];
            maxage = oldest->age;
        }
    }
    return oldest;
}

// Memory access within a set for a specific tag
int has_block(struct Block* setptr, int Eval, mem_addr_t tag){
    for(int i=0; i<Eval; i++){
        if(setptr[i].tag == tag && setptr[i].valid){
            // Time since access now 0
            setptr[i].age = 0;
            return 1;
        }
    }
    return 0;
}

void simulate_cache(int sval, int bval, int Eval, FILE* tracefile, int verbose)
{
    char reftype;
    mem_addr_t addr;
    unsigned length;

    // Counter var
    int hits=0;
    int miss=0;
    int evict=0;
    
    int cache_size = pow(2,sval) * Eval * sizeof(struct Block);
    struct Block* cache = (struct Block *)malloc(cache_size);

    // init all valid bits to 0 and age to max int
    for(int i=0; i < pow(2,sval) * Eval; i++){
        cache[i].valid = 0;
        cache[i].age = 0x7fffffff;
    }
    
    while (read_reference(tracefile, &reftype, &addr, &length, verbose)) {
        // Skip I reftype
        if(reftype == 'I'){ continue; }

        mem_addr_t tag = addr >> (sval + bval);
        mem_addr_t set = addr << ((sizeof(mem_addr_t) * 8)-(sval+bval));
        set >>= (sizeof(mem_addr_t) * 8) - sval;

        struct Block* setptr = cache + (set * Eval);

        // Inc age if valid block
        for(int i=0; i<Eval; i++){
            if(setptr[i].valid == 1){
                setptr[i].age++;
            }
        }

        // Try access
        if(has_block(setptr, Eval, tag) == 1){ 
            hits++;
        } else{
            struct Block* replace = get_replace(setptr, Eval);
            if(replace->valid == 1){
                evict++;
            }
            miss++;
            replace->tag = tag;
            replace->age = 0;
            replace->valid = 1;
        }
        // Modify instructions will always add an extra hit on write
        if(reftype=='M'){hits++;}

        if (verbose)
	    printf("%c %#08lx\n", reftype, addr);
    }
    printSummary(hits, miss, evict);

    free(cache);
}

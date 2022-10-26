using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class GenMaze : MonoBehaviour
{
    [SerializeField] private int Size;
    // easy = 20
    // normal = 30
    // hard = 40
    // insane = 60
    [SerializeField] private int CrawlFrequency;
    [SerializeField] private float StrutChance;
    [SerializeField] private float LampChance;
    [SerializeField] private float PathWidth;
    [SerializeField] private float PathHeight;
    [SerializeField] private Material mazeMat1;
    [SerializeField] private Material mazeMat2;
    [SerializeField] private Material startMat;
    [SerializeField] private Material treasureMat;
    [SerializeField] private GameObject Key;
    [SerializeField] private GameObject End;
    [SerializeField] private GameObject Crouch;
    [SerializeField] private GameObject Strut;
    [SerializeField] private GameObject Lamp;
    public float placementThreshold;

    // Start is called before the first frame update
    void Start()
    {
        //Instantiate(Spawn, new Vector3(0, 0,0), Quaternion.identity);
        placementThreshold = .1f;
        int[,] data = addStuff(FromDimensions(Size,Size));

        //printmaze(data);
        DisplayMaze(data);

    }

    // Update is called once per frame
    void Update()
    {
        
    }

    // generates the maze from the dimensions
    int[,] FromDimensions(int width, int height){
        int[,] maze = new int[width, height];
        int rMax = maze.GetUpperBound(0);
        int cMax = maze.GetUpperBound(1);
        for (int i = 0; i <= rMax; i++)
        {
            for (int j = 0; j <= cMax; j++)
            {
                if (i == 0 || j == 0 || i == rMax || j == cMax)
                {
                    // always generates walls around outside edge
                    maze[i, j] = 1;
                }

                else if (i % 2 == 0 && j % 2 == 0)
                {
                    if (Random.value > placementThreshold)
                    {
                        // builds a wall
                        maze[i, j] = 1;

                        // selects new sopt to check randomly
                        int a = Random.value < .5 ? 0 : (Random.value < .5 ? -1 : 1);
                        int b = a != 0 ? 0 : (Random.value < .5 ? -1 : 1);
                        // extends wall in random direction
                        maze[i+a, j+b] = 1;
                    }
                }
            }
        }

        // create start 
        maze[1,1] = 0;
        maze[1,2] = 0;
        maze[2,1] = 0;
        maze[2,2] = 0;

        // create exit
        return maze;
    }

    int[,] addStuff(int[,] maze){
        bool end = false;
        bool key = false;
        int crouchCount = 0;
        int lampCount = 0;
        int rMax = maze.GetUpperBound(0);
        int cMax = maze.GetUpperBound(1);

        
        // 2 = end
        // 3 = key card
        // 4 = crouch
        // 5 = strutRot1
        // 6 = strutRot2
        // 7 = lamp
        int x,y;
        while (!end){
            x = Random.Range(0, Size);
            y = Random.Range(0, Size);
            if (maze[x,y] == 0){
                maze[x,y] = 2;
                end = true;
            }
        }

        while (!key){
            x = Random.Range(0, Size);
            y = Random.Range(0, Size);
            if (maze[x,y] == 0){
                maze[x,y] = 3;
                key = true;
            }
        }

        while (crouchCount < Size/5){
            x = Random.Range(0, Size);
            y = Random.Range(0, Size);
            if (maze[x,y] == 0){
                maze[x,y] = 4;
                crouchCount++;
            }
        }

        // add celing struts
        for (int i = 1; i <= rMax-1; i++)
        {
            for (int j = 1; j <= cMax-1; j++)
            {

                if (maze[i,j] == 0 && maze[i+1,j] == 1 && maze[i-1,j] == 1){
                    maze[i,j] = 5;
                }
                if (maze[i,j] == 0 && maze[i,j-1] == 1 && maze[i,j+1] == 1){
                    maze[i,j] = 6;
                }
            }
        }

        while (lampCount < Size * LampChance){
            x = Random.Range(0, Size);
            y = Random.Range(0, Size);
            if (maze[x,y] != 1){
                maze[x,y] = 7;
                lampCount++;
            }
        }
        return(maze);
    }

    void printmaze(int[,] data){
        string line;
        int rMax = data.GetUpperBound(0);
        int cMax = data.GetUpperBound(1);
        for (int i = 0; i <= rMax; i++)
        {
            line = "";
            for (int j = 0; j <= cMax; j++)
            {
                line = line + data[i,j].ToString();
            }
            Debug.Log(line);
        }
    }

    //Creates the maze mesh from the provided data
    public Mesh FromData(int[,] data){
        Mesh maze = new Mesh();

        List<Vector3> newVertices = new List<Vector3>();
        List<Vector2> newUVs = new List<Vector2>();

        maze.subMeshCount = 2;
        List<int> floorTriangles = new List<int>();
        List<int> wallTriangles = new List<int>();

        int rMax = data.GetUpperBound(0);
        int cMax = data.GetUpperBound(1);
        float halfH = PathHeight * .5f;

        for (int i = 0; i <= rMax; i++)
        {
            for (int j = 0; j <= cMax; j++)
            {   
                float rot1 = (float)Random.Range(-10f,10f);
                float rot2 = (float)Random.Range(-7f,7f);
                float ranHeight = (float)Random.Range(.3f,0.5f);

                // add end
                if (data[i, j] == 2){
                    Debug.Log("Spawned an end");
                    Instantiate(End, new Vector3(j * PathWidth, 0, i * PathWidth), Quaternion.identity);
                }

                // add key
                if (data[i, j] == 3){
                    Debug.Log("Spawned a key");
                    Instantiate(Key, new Vector3(j * PathWidth, 0, i * PathWidth), Quaternion.identity);
                }

                // add crouch
                if (data[i, j] == 4){
                    Debug.Log("Spawned a crouch");
                    Instantiate(Crouch, new Vector3(j * PathWidth, 2, i * PathWidth), Quaternion.identity);
                }
                
                if (data[i, j] == 7){

                    Instantiate(Lamp, new Vector3(j * PathWidth, PathHeight -.08f, i * PathWidth), Quaternion.identity);
                }

                if (data[i, j] != 1)
                {
                    // floor
                    AddQuad(Matrix4x4.TRS(
                        new Vector3(j * PathWidth, 0, i * PathWidth),
                        Quaternion.LookRotation(Vector3.up),
                        new Vector3(PathWidth, PathWidth, 1)
                    ), ref newVertices, ref newUVs, ref floorTriangles);

                    // ceiling
                    AddQuad(Matrix4x4.TRS(
                        new Vector3(j * PathWidth, PathHeight, i * PathWidth),
                        Quaternion.LookRotation(Vector3.down),
                        new Vector3(PathWidth, PathWidth, 1)
                    ), ref newVertices, ref newUVs, ref floorTriangles);


                    // walls on sides next to blocked grid cells

                    if (i - 1 < 0 || data[i-1, j] == 1)
                    {
                        AddQuad(Matrix4x4.TRS(
                            new Vector3(j * PathWidth, halfH, (i-.5f) * PathWidth),
                            Quaternion.LookRotation(Vector3.forward),
                            new Vector3(PathWidth, PathHeight, 1)
                        ), ref newVertices, ref newUVs, ref wallTriangles);
                    }

                    if (j + 1 > cMax || data[i, j+1] == 1)
                    {
                        AddQuad(Matrix4x4.TRS(
                            new Vector3((j+.5f) * PathWidth, halfH, i * PathWidth),
                            Quaternion.LookRotation(Vector3.left),
                            new Vector3(PathWidth, PathHeight, 1)
                        ), ref newVertices, ref newUVs, ref wallTriangles);
                    }

                    if (j - 1 < 0 || data[i, j-1] == 1)
                    {
                        AddQuad(Matrix4x4.TRS(
                            new Vector3((j-.5f) * PathWidth, halfH, i * PathWidth),
                            Quaternion.LookRotation(Vector3.right),
                            new Vector3(PathWidth, PathHeight, 1)
                        ), ref newVertices, ref newUVs, ref wallTriangles);
                    }

                    if (i + 1 > rMax || data[i+1, j] == 1)
                    {
                        AddQuad(Matrix4x4.TRS(
                            new Vector3(j * PathWidth, halfH, (i+.5f) * PathWidth),
                            Quaternion.LookRotation(Vector3.back),
                            new Vector3(PathWidth, PathHeight, 1)
                        ), ref newVertices, ref newUVs, ref wallTriangles);
                    }
                }
            }
        }

        maze.vertices = newVertices.ToArray();
        maze.uv = newUVs.ToArray();
        
        maze.SetTriangles(floorTriangles.ToArray(), 0);
        maze.SetTriangles(wallTriangles.ToArray(), 1);

        //5
        maze.RecalculateNormals();

        return maze;
    }

    private void AddQuad(Matrix4x4 matrix, ref List<Vector3> newVertices,
        ref List<Vector2> newUVs, ref List<int> newTriangles)
    {
        int index = newVertices.Count;

        // corners before transforming
        Vector3 vert1 = new Vector3(-.5f, -.5f, 0);
        Vector3 vert2 = new Vector3(-.5f, .5f, 0);
        Vector3 vert3 = new Vector3(.5f, .5f, 0);
        Vector3 vert4 = new Vector3(.5f, -.5f, 0);

        newVertices.Add(matrix.MultiplyPoint3x4(vert1));
        newVertices.Add(matrix.MultiplyPoint3x4(vert2));
        newVertices.Add(matrix.MultiplyPoint3x4(vert3));
        newVertices.Add(matrix.MultiplyPoint3x4(vert4));

        newUVs.Add(new Vector2(1, 0));
        newUVs.Add(new Vector2(1, 1));
        newUVs.Add(new Vector2(0, 1));
        newUVs.Add(new Vector2(0, 0));

        newTriangles.Add(index+2);
        newTriangles.Add(index+1);
        newTriangles.Add(index);

        newTriangles.Add(index+3);
        newTriangles.Add(index+2);
        newTriangles.Add(index);
    }

    //Display the created mesh
    private void DisplayMaze(int[,] data){
        GameObject go = new GameObject();
        go.transform.position = Vector3.zero;
        go.name = "Procedural Maze";
        go.layer = 7;

        MeshFilter mf = go.AddComponent<MeshFilter>();
        mf.mesh = FromData(data);
        
        MeshCollider mc = go.AddComponent<MeshCollider>();
        mc.sharedMesh = mf.mesh;

        MeshRenderer mr = go.AddComponent<MeshRenderer>();
        mr.materials = new Material[2] {mazeMat1, mazeMat2};
    }
}

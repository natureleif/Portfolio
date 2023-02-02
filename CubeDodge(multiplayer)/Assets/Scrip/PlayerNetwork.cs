using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Unity.Netcode;
using UnityEngine.SceneManagement;

public class PlayerNetwork : NetworkBehaviour
{
    [SerializeField] float moveSpeed = 5;
    [SerializeField] float jumpPower = 8;
    [SerializeField] float boostPower = 10;
    [SerializeField] Transform groundCheck;
    [SerializeField] LayerMask ground;
    [SerializeField] LayerMask enemy;
    [SerializeField] LayerMask player;

    //Audio
    [SerializeField] private AudioSource JumpSound;
    [SerializeField] private AudioSource PlayerCollideSound;
    [SerializeField] private AudioSource EnemyCollideSound;
    [SerializeField] private AudioSource BoostSound;
    [SerializeField] private AudioSource ModeStart;
    [SerializeField] private AudioSource ModeEnd;
    [SerializeField] private AudioSource BoostCharged;

    Rigidbody client_rb;
    NetworkObject otherPlayer;
    bool noJump = false;
    bool reverse = false;
    float timeStamp;
    bool hasBoost = true;
    bool hasStarted = false;
    Vector3 direction;

    // Start is called before the first frame update
    void Start()
    {
        if(!IsOwner){return;}
        client_rb = GetComponent<Rigidbody>();
        gameObject.layer = 9;
    }

    // Update is called once per frame
    void Update()
    {   
        if(!IsOwner){return;}
        if(isGrounded() && !hasStarted){
            hasStarted = true;
            boostRecharge();
        }

        float horizontalInput = Input.GetAxis("Horizontal");
        float verticalInput = Input.GetAxis("Vertical");

        if(reverse){
            horizontalInput = horizontalInput * -1f;
            verticalInput = verticalInput * -1f;
        }
        
        if(Time.fixedTime - timeStamp > .6f){ // checks if player is still in ragdoll
            client_rb.velocity = new Vector3(horizontalInput * moveSpeed, client_rb.velocity.y, verticalInput * moveSpeed);
        }
        
        if(Input.GetButtonDown("Jump") && isGrounded() && !noJump){
            jump();
        }

        if (Input.GetKeyDown("e") && hasBoost){
            boost();
        }

        Collider[] hits;
        hits = Physics.OverlapSphere(gameObject.transform.position, .8f, player);
        if(hits.Length > 0 && Time.fixedTime - timeStamp > 1f){
            timeStamp = Time.fixedTime;
            direction = client_rb.transform.position - hits[0].transform.position;
            Invoke(nameof(pushback), .2f); // Delay helps the desync catch up
        }
    }

    void FixedUpdate(){
        if(!IsOwner){return;}
        int random = Random.Range(0, 2000);
        if(random < 1 && !noJump && !reverse && hasStarted){
            GameObject canvas = gameObject.transform.GetChild(2).gameObject;
            canvas.transform.GetChild(2).gameObject.SetActive(true);
            ModeStart.Play();
            noJump = true;
            Invoke(nameof(endNoJump), 8);
        }
        if(random > 1998 && !reverse && !noJump && hasStarted){
            GameObject canvas = gameObject.transform.GetChild(2).gameObject;
            canvas.transform.GetChild(4).gameObject.SetActive(true);
            ModeStart.Play();
            reverse = true;
            Invoke(nameof(endReverse), 8);
        }
    }

    void pushback(){
        direction.Normalize();
        PlayerCollideSound.Play();
        client_rb.AddForce(direction * 3f,ForceMode.Impulse);
    }
    
    private void OnCollisionEnter(Collision collision) {
        if (collision.gameObject.tag == "SpecialEnemy"){
            EnemyCollideSound.Play();
            timeStamp = Time.fixedTime;
        }
    }
    
    void jump(){
        JumpSound.Play();
        client_rb.velocity = new Vector3(client_rb.velocity.x,jumpPower,client_rb.velocity.z);
    }

    void boost(){
        BoostSound.Play();
        client_rb.velocity = new Vector3(client_rb.velocity.x,boostPower,client_rb.velocity.z);
        GameObject canvas = gameObject.transform.GetChild(2).gameObject;
        canvas.transform.GetChild(3).gameObject.SetActive(false);
        hasBoost = false;
        Invoke(nameof(boostRecharge), 4f);
    }

    bool isGrounded(){
        bool test1 = Physics.CheckSphere(groundCheck.position, .3f, ground);
        bool test2 = Physics.CheckSphere(groundCheck.position, .3f, enemy);
        bool test3 = Physics.CheckSphere(groundCheck.position, .3f, player);
        return test1 || test2 || test3;
    }

    void endNoJump(){
        GameObject canvas = gameObject.transform.GetChild(2).gameObject;
        canvas.transform.GetChild(2).gameObject.SetActive(false);
        ModeEnd.Play();
        noJump = false;
    }

    void endReverse(){
        GameObject canvas = gameObject.transform.GetChild(2).gameObject;
        canvas.transform.GetChild(4).gameObject.SetActive(false);
        ModeEnd.Play();
        reverse = false;
    }

    void boostRecharge(){
        hasBoost = true;
        GameObject canvas = gameObject.transform.GetChild(2).gameObject;
        canvas.transform.GetChild(3).gameObject.SetActive(true);
        BoostCharged.Play();
    }
}

using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Unity.Netcode;

public class PlayerClient : MonoBehaviour
{
    [SerializeField] float moveSpeed = 6;
    [SerializeField] float jumpPower = 8;
    [SerializeField] Transform groundCheck;
    [SerializeField] LayerMask ground;
    [SerializeField] LayerMask enemy;
    [SerializeField] float SyncThreshold = 1f;
    
    //[SerializeField] GameObject Player2Spawn; TODO
    Rigidbody client_rb;

    // Start is called before the first frame update
    void Start()
    {
        //TODO set client player color
        client_rb = GetComponent<Rigidbody>();
    }

    // Update is called once per frame
    void Update()
    {
        float horizontalInput = Input.GetAxis("Horizontal");
        float verticalInput = Input.GetAxis("Vertical");

        client_rb.velocity = new Vector3(horizontalInput * moveSpeed, client_rb.velocity.y, verticalInput * moveSpeed);

        if(client_rb.transform.position.y < -10f){
            client_rb.transform.position = new Vector3(0f,1.5f,0f);//Player2Spawn.transform.position; TODO
        }

        if(Input.GetButtonDown("Jump") && isGrounded()){
            jump(jumpPower);
        }

        //TODO sync with ghost if velocity low or possibly no input (try both)
        NetworkObject playerGhost = NetworkManager.Singleton.LocalClient.PlayerObject;
        float distance = Vector3.Distance(playerGhost.transform.position, client_rb.transform.position);
        
        client_rb.transform.position = playerGhost.transform.position;
        //Debug.Log(distance);
        //distance > SyncThreshold
        if(horizontalInput <.1f && verticalInput < .1f && distance > SyncThreshold){ // if ghost isnt moving
            Debug.Log("Sync");
            client_rb.transform.position = playerGhost.transform.position;
        }
    }

    void jump(float power){
        client_rb.velocity = new Vector3(client_rb.velocity.x,power,client_rb.velocity.y);
    }

    bool isGrounded(){
        bool test3 = client_rb.transform.position.y > 0f;
        bool test1 = Physics.CheckSphere(groundCheck.position, .45f, ground);
        bool test2 = Physics.CheckSphere(groundCheck.position, .45f, enemy);
        return (test1 || test2) && test3;
    }
}


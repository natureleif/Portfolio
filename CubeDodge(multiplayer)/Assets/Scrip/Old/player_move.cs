using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class player_move : MonoBehaviour
{
    [SerializeField] float moveSpeed;
    [SerializeField] float jumpPower;
    [SerializeField] Transform groundCheck;
    [SerializeField] LayerMask ground;
    [SerializeField] AudioSource jumpSound;
    [SerializeField] AudioSource killSound;
    Rigidbody rb; 
    // Start is called before the first frame update
    void Start()
    {
        Debug.Log("hellow from start");
        rb = GetComponent<Rigidbody>();
    }

    // Update is called once per frame
    void Update()
    {
        float horizontalInput = Input.GetAxis("Horizontal");
        float verticalInput = Input.GetAxis("Vertical");

        rb.velocity = new Vector3(horizontalInput * moveSpeed, rb.velocity.y, verticalInput *moveSpeed);

        if(Input.GetButtonDown("Jump") && isGrounded()){
            jump(jumpPower);
        }  
    }

    void jump(float power){
        rb.velocity = new Vector3(rb.velocity.x,power,rb.velocity.y);
        jumpSound.Play();
    }

    private void OnCollisionEnter(Collision other){
        if (other.gameObject.CompareTag("EnemyHead")){
            Destroy(other.transform.parent.gameObject);
            killSound.Play();
        }
    }

    bool isGrounded(){
        return Physics.CheckSphere(groundCheck.position, 0.2f, ground);
    }

}

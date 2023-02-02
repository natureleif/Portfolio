using System.Collections;
using System.Collections.Generic;
using Unity.Netcode;
using UnityEngine;


public class EnemyMove : MonoBehaviour 
{
    float speed = -7f;
    Rigidbody rb;
    
    // Start is called before the first frame update
    void Start()
    {
        rb = GetComponent<Rigidbody>();
    }

    // Update is called once per frame
    void Update()
    {
        rb.velocity = new Vector3 (0f,0f,speed);
        if (rb.transform.position.z < -10f){
            DestroyObject(gameObject);
        }
    }
}

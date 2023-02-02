using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;

public class player_life : MonoBehaviour
{
    bool ded = false;
    [SerializeField] AudioSource dedSound;

    private void Update(){
        if(transform.position.y < -3f && !ded){
            die();
        }
    }

    private void OnCollisionEnter(Collision other) {
        if (other.gameObject.CompareTag("EnemyBody") && !ded){
            GetComponent<MeshRenderer>().enabled = false;
            GetComponent<Rigidbody>().isKinematic = true;
            GetComponent<player_move>().enabled = false;
            die();
        } 
    }

    void die(){
        ded = true;
        dedSound.Play();
        Invoke(nameof(restart), .5f);
        
    }

    void restart(){
        // SceneManager.LoadScene(SceneManager.GetActiveScene().name);
    }
}

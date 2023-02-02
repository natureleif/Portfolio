using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.SceneManagement;


public class finish_level : MonoBehaviour
{
    private void OnTriggerEnter(Collider other){
        if (other.gameObject.name == "player"){
            SceneManager.LoadScene(SceneManager.GetActiveScene().name);
        }
    }
}

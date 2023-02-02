using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class collect : MonoBehaviour
{
    int score = 0;
    [SerializeField] Text scoreText;
    [SerializeField] AudioSource coinSound;

    private void OnTriggerEnter(Collider other){
        if(other.gameObject.CompareTag("Coin")){
            Destroy(other.gameObject);
            coinSound.Play();
            score++;
            scoreText.text = "Score: " + score;
        }
    }
}

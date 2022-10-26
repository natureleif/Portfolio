using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class LightFlicker : MonoBehaviour
{
    public bool isFlickering = false;
    public float timeDelay;
    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {   
        if (isFlickering == false){
            StartCoroutine(FlickeringLight());

        }

        IEnumerator FlickeringLight(){
            isFlickering = true;
            for(int i = 0; i<Random.Range(1,6);i++){
                this.gameObject.GetComponent<Light>().enabled = false;
                timeDelay = Random.Range(0.01f,0.5f);
                yield return new WaitForSeconds(timeDelay);
                this.gameObject.GetComponent<Light>().enabled = true;
                timeDelay = Random.Range(0.01f,0.2f);
                yield return new WaitForSeconds(timeDelay);
            }
            timeDelay = Random.Range(1f,20f);
            yield return new WaitForSeconds(timeDelay);
            isFlickering = false;
        }
    }
}

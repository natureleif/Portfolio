using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class rotate : MonoBehaviour
{
    [SerializeField] float speedX = 0f;
    [SerializeField] float speedY = 0.5f;
    [SerializeField] float speedZ = 0f;

    // Update is called once per frame
    void Update()
    {
        transform.Rotate(360 * Time.deltaTime * speedX, 360 * Time.deltaTime * speedY, 360 * Time.deltaTime * speedZ);
    }
}

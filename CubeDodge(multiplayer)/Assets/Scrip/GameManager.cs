using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Unity.Netcode;
using UnityEngine.SceneManagement;


public class GameManager : NetworkBehaviour
{
    [SerializeField] private GameObject Player1Spawn;
    [SerializeField] private GameObject Player2Spawn;
    [SerializeField] private string NextScene;
    private float timeStamp;
    private float startTime;
    private float enemyDelay;
    NetworkObject player;
    bool finished = false;
    
    void Start()
    {
        timeStamp = Time.fixedTime;
        startTime = Time.fixedTime;
        enemyDelay = 4f;

        player = NetworkManager.Singleton.LocalClient.PlayerObject;
        Rigidbody playerRig = player.GetComponent<Rigidbody>();
        player.transform.GetChild(0).gameObject.SetActive(true); //activate camera on player
        
        Renderer playerColor = player.GetComponent<Renderer>();
        playerColor.material.color = new Color(0f, .7f, 1f, 0f);

        clearUI();

        if(IsServer){
            playerRig.velocity = new Vector3(0,0,0);
            player.transform.position = Player1Spawn.transform.position;
        }

        if(!IsServer){
            playerRig.velocity = new Vector3(0,0,0);
            player.transform.position = Player2Spawn.transform.position;
        }
    }

    void clearUI(){
        NetworkObject playerObj = NetworkManager.Singleton.LocalClient.PlayerObject;
        GameObject canvas = playerObj.transform.GetChild(2).gameObject;
        canvas.transform.GetChild(0).gameObject.SetActive(false);
        canvas.transform.GetChild(1).gameObject.SetActive(false);
    }

    void FixedUpdate()
    {
        if(player.transform.position.y < -10 && !finished){
            finished = true;
            nextLevelServerRpc();
    
        }
        
        if(!IsServer){return;}
        if(Time.fixedTime - timeStamp > enemyDelay){
            timeStamp = Time.fixedTime;
            int random = Random.Range(0, 99);
            if(random > 10){
                enemyClientRpc(new Vector3(Random.Range(-7, 7),Random.Range(-.9f, 4),Random.Range(30, 60)));
            }
            if (random < 10){
                specialEnemyClientRpc(new Vector3(Random.Range(-7, 7),Random.Range(-.9f, 4),Random.Range(30, 60)));
            }
        }
        if (enemyDelay > .2f){
            enemyDelay -= .001f;
        }
    }

    [ClientRpc]
    private void enemyClientRpc(Vector3 spawnLocation){
        GameObject temp = Instantiate(Resources.Load("Enemy") as GameObject, spawnLocation, Quaternion.Euler(new Vector3(-90, 0, 0)));
    }

    [ClientRpc]
    private void specialEnemyClientRpc(Vector3 spawnLocation){
        GameObject temp = Instantiate(Resources.Load("SpecialEnemy") as GameObject, spawnLocation, Quaternion.Euler(new Vector3(-90, 0, 0)));
    }

    [ClientRpc]
    void winUIClientRpc(int player){
        NetworkObject playerObj = NetworkManager.Singleton.LocalClient.PlayerObject;
        GameObject canvas = playerObj.transform.GetChild(2).gameObject;
        if(player == 1){canvas.transform.GetChild(0).gameObject.SetActive(true);} // TODO client doesnt have these variables
        if(player == 2){canvas.transform.GetChild(1).gameObject.SetActive(true);}
    }

    [ServerRpc(RequireOwnership = false)]
    private void nextLevelServerRpc(){
        NetworkManager.Singleton.SceneManager.LoadScene(NextScene, LoadSceneMode.Single);
    }
}

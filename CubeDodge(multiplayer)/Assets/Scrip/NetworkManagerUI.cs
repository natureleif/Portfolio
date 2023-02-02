using System.Collections;
using System.Collections.Generic;
using System.Threading.Tasks;
using TMPro;
using Unity.Netcode;
using Unity.Netcode.Transports.UTP;
using Unity.Services.Authentication;
using Unity.Services.Core;
using Unity.Services.Relay;
using Unity.Services.Relay.Models;
using UnityEngine;
using UnityEngine.UI;
using UnityEngine.SceneManagement;

public class NetworkManagerUI : NetworkBehaviour
{
    [SerializeField]private Button playBtn;
    [SerializeField]private Button hostBtn;
    [SerializeField]private Button clientBtn;
    [SerializeField]private Button copyBtn;

    [SerializeField] private TMP_Text joinCodeText;
    [SerializeField] private GameObject background;
    [SerializeField] private TMP_InputField joinInput;
    
    private UnityTransport transport;
    private const int MaxPlayers = 2;
    private bool isConnected = false;

    // Start is called before the first frame update
    private async void Awake()
    {
        //hide server UI options
        playBtn.gameObject.SetActive(false);
        hostBtn.gameObject.SetActive(false);
        clientBtn.gameObject.SetActive(false);
        joinCodeText.gameObject.SetActive(false);
        joinInput.gameObject.SetActive(false);
        copyBtn.gameObject.SetActive(false);

        transport = FindObjectOfType<UnityTransport>();
        await Authenticate();

        //show options
        hostBtn.gameObject.SetActive(true);
        clientBtn.gameObject.SetActive(true);
        joinInput.gameObject.SetActive(true);

        // start host on host button click
        hostBtn.onClick.AddListener(() =>{
            CreateGame();
        });

        // start client on client button click
        clientBtn.onClick.AddListener(() =>{
            JoinGame();
        });

        playBtn.onClick.AddListener(() =>{
            StartGame();
        });

        copyBtn.onClick.AddListener(() =>{
            CopyToClipboard(joinCodeText.text);
        });
    }

    void Update(){
        if(IsServer){
            if(isConnected){
                int num = NetworkManager.Singleton.ConnectedClientsIds.Count;
                playerCountClientRpc(num);
            }
        }
    }

    private static async Task Authenticate(){
        await UnityServices.InitializeAsync();
        await AuthenticationService.Instance.SignInAnonymouslyAsync();
    }

    public async void CreateGame(){

        try{
        joinCodeText.text = "";
        joinCodeText.gameObject.SetActive(true);
        Allocation a = await RelayService.Instance.CreateAllocationAsync(MaxPlayers);
        joinCodeText.text = await RelayService.Instance.GetJoinCodeAsync(a.AllocationId);

        transport.SetHostRelayData(a.RelayServer.IpV4, (ushort)a.RelayServer.Port, a.AllocationIdBytes, a.Key, a.ConnectionData);
        NetworkManager.Singleton.StartHost();

        playBtn.gameObject.SetActive(true);
        hostBtn.gameObject.SetActive(false);
        clientBtn.gameObject.SetActive(false);
        joinInput.gameObject.SetActive(false);
        copyBtn.gameObject.SetActive(true);

        isConnected = true;
        }
        catch{joinCodeText.text = "Failed to create lobby";}
    }

    public async void JoinGame(){
        joinCodeText.text = "";
        joinCodeText.gameObject.SetActive(true);

        try {
        JoinAllocation a = await RelayService.Instance.JoinAllocationAsync(joinInput.text.ToUpper());

        transport.SetClientRelayData(a.RelayServer.IpV4, (ushort)a.RelayServer.Port, a.AllocationIdBytes, a.Key, a.ConnectionData, a.HostConnectionData);

        NetworkManager.Singleton.StartClient();
        hostBtn.gameObject.SetActive(false);
        joinCodeText.text = ("Connected to " + joinInput.text);
        clientBtn.gameObject.SetActive(false);
        isConnected = true;

        }
        catch{ joinCodeText.text = "Failed to connect"; }
    }

    public void StartGame(){
        NetworkManager.Singleton.SceneManager.LoadScene("sky1", LoadSceneMode.Single);
    }

    
    [ClientRpc]
    private void playerCountClientRpc(int num){
        GameObject.Find("PlayerCount").GetComponent<Text>().text = "Players in Lobby: " + num;
    }
    

    public static void CopyToClipboard(string s){
        TextEditor te = new TextEditor();
        te.text = s;
        te.SelectAll();
        te.Copy();
    }
}

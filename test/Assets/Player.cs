using UnityEngine;
using System.Collections;
using System.IO;
[System.Serializable]
public class Player : MonoBehaviour {

    public int health = 100;

    // Use this for initialization
    void Start () {

        Debug.Log(Application.persistentDataPath);
    }
	
	// Update is called once per frame
	void Update () {
        if (Input.GetKeyDown(KeyCode.Q))
        {
            Time.timeScale = 0.5f;
        }
        if (Input.GetKeyDown(KeyCode.E))
        {
            Time.timeScale = 2f;
        }
        if (Input.GetKeyDown(KeyCode.R))
        {
            Time.timeScale = 1f;
        }
        if (Input.GetKeyDown(KeyCode.F))
        {
            SaveLoad.Save();
        }

    }
}

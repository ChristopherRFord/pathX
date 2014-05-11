/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package PathXData;

import PathX.PathX.PathXPropertyType;
import static PathX.PathXConstants.PATH_DATA;
import PathX.PathXGame;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import properties_manager.PropertiesManager;

/**
 *
 * @author Chris
 */
public class PathXFileManager
{
    private PathXGame game;
    private PathXDataModel data;
    
    public PathXFileManager(PathXGame game, PathXDataModel data)
    {
        this.game = game;
        this.data = data;
    }
    
    public void saveGame()
    {
         try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(PathXPropertyType.FILE_PLAYER_RECORD);
            File fileToOpen = new File(recordPath);
            

            // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            DataOutputStream dos = new DataOutputStream(baos);
            dos.writeInt(data.getBalance());
            dos.writeInt(data.currentLevel);
            byte[] bytes = baos.toByteArray();
            FileOutputStream fos = new FileOutputStream(fileToOpen);
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            
            bos.write(bytes);
            
            
            bos.close();
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }  
                
    }
    
    public void loadGame()
    {
        try
        {
            PropertiesManager props = PropertiesManager.getPropertiesManager();
            String recordPath = PATH_DATA + props.getProperty(PathXPropertyType.FILE_PLAYER_RECORD);
            File fileToOpen = new File(recordPath);
            
             // LET'S USE A FAST LOADING TECHNIQUE. WE'LL LOAD ALL OF THE
            // BYTES AT ONCE INTO A BYTE ARRAY, AND THEN PICK THAT APART.
            // THIS IS FAST BECAUSE IT ONLY HAS TO DO FILE READING ONCE
            byte[] bytes = new byte[Long.valueOf(fileToOpen.length()).intValue()];
            ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
            FileInputStream fis = new FileInputStream(fileToOpen);
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            // HERE IT IS, THE ONLY READY REQUEST WE NEED
            bis.read(bytes);
            bis.close();
            
            // NOW WE NEED TO LOAD THE DATA FROM THE BYTE ARRAY
            DataInputStream dis = new DataInputStream(bais);
            
            // NOTE THAT WE NEED TO LOAD THE DATA IN THE SAME
            // ORDER AND FORMAT AS WE SAVED IT
            // FIRST READ THE NUMBER OF LEVELS
            
            data.balance = dis.readInt();
            data.currentLevel = dis.readInt();
        }
        catch(Exception e)
        {
            // THERE WAS NO RECORD TO LOAD, SO WE'LL JUST RETURN AN
            // EMPTY ONE AND SQUELCH THIS EXCEPTION
        }  
    }
}

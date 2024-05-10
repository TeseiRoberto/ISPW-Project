package com.rt.ispwproject.factories;

import com.rt.ispwproject.Main;
import com.rt.ispwproject.dao.profiledao.ProfileDao;
import com.rt.ispwproject.dao.profiledao.ProfileDaoFileSystem;
import com.rt.ispwproject.dao.profiledao.ProfileDaoMysql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ProfileDaoFactory {

    private static ProfileDaoFactory    instance;
    private final boolean               useMysql;


    private ProfileDaoFactory(boolean useMysql)
    {
        this.useMysql = useMysql;
    }


    public static ProfileDaoFactory getInstance()
    {
        if(instance == null)
        {
            boolean useMysql = loadDaoTypeFromConfigurationFile();
            instance = new ProfileDaoFactory(useMysql);
        }

        return instance;
    }


    // Reads the configuration file to determine which dao (Mysql or FileSystem) should be used
    private static boolean loadDaoTypeFromConfigurationFile()
    {
        try (InputStream configFile = new FileInputStream(Main.CONFIG_FILE_NAME))
        {
            Properties config = new Properties();
            config.load(configFile);

            if(config.getProperty("PERSISTENCE_TYPE").equals("mysql"))         // Set persistence type
                return true;

        } catch(IOException ignored)
        {
            return false;
        }

        return false;
    }


    // Creates an instance of the ProfileDao
    public ProfileDao createDao()
    {
        if(useMysql)
            return new ProfileDaoMysql();

        return new ProfileDaoFileSystem();
    }

}

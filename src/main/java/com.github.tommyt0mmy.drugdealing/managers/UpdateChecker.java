package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

public class UpdateChecker
{
    private final DrugDealing plugin = DrugDealing.getInstance();
    private final String current_version = plugin.getDescription().getVersion();
    private String spigot_url = "https://api.spigotmc.org/legacy/update.php?resource=%d";
    private boolean needs_update = false;
    private boolean network_error = false;
    private boolean invalid_resource_error = false;
    private String latest_version = "";

    public UpdateChecker()
    {
        check_for_updates();
    }

    public void check_for_updates()
    {
        try
        {
            //connection
            spigot_url = String.format(spigot_url, plugin.getSpigotResourceId());
            URL url = new URL(spigot_url);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            //checking
            try
            {
                if (con.getResponseCode() != 200)
                {
                    network_error = true;
                    return;
                }
            } catch (UnknownHostException UHException)
            {
                network_error = true;
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                content.append(inputLine);
            }

            if (content.toString().equals("Invalid resource"))
            {
                invalid_resource_error = true;
                return;
            }

            latest_version = content.toString();

            if (!current_version.equals(latest_version))
                needs_update = true;

        } catch (IOException e) { e.printStackTrace(); }
    }

    public boolean needsUpdate()
    {
        return needs_update;
    }

    public boolean networkError()
    {
        return network_error;
    }

    public boolean invalidResourceError() { return invalid_resource_error; }

    public String getCurrent_version()
    {
        return current_version;
    }

    public String getLatest_version()
    {
        return latest_version;
    }
}

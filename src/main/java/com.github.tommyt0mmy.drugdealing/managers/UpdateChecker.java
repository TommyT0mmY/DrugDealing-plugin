package com.github.tommyt0mmy.drugdealing.managers;

import com.github.tommyt0mmy.drugdealing.DrugDealing;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class UpdateChecker
{
    private final DrugDealing plugin = DrugDealing.getInstance();
    private final String current_version = plugin.getDescription().getVersion();
    private String spigot_url = "https://api.spigotmc.org/legacy/update.php?resource=%d";
    private boolean needs_update = false;
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
            if (con.getResponseCode() != 200)
                return;

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null)
            {
                content.append(inputLine);
            }

            if (content.toString().equals("Invalid resource"))
                return;

            latest_version = content.toString();

            if (!current_version.equals(latest_version))
                needs_update = true;

        } catch (IOException e) { e.printStackTrace(); }
    }

    public boolean needsUpdate()
    {
        return needs_update;
    }

    public String getCurrent_version()
    {
        return current_version;
    }

    public String getLatest_version()
    {
        return latest_version;
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.brandts.jazzbot.spotify.SpotifyController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Teddy
 */
public class SpotifyClientHandler {

    
    
    public static boolean pause() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", "tell application \"Spotify\" to pause"};
            Process process = runtime.exec(args);
            process.waitFor(100, TimeUnit.MILLISECONDS);

        } catch (IOException ex) {
            System.err.println("Could not puase");
        } catch (InterruptedException ex) {
            System.err.println("Error" + ex);
        } finally {
        }
        return true;

    }

    public static boolean play() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", "tell application \"Spotify\" to play"};
            Process process = runtime.exec(args);
            process.waitFor();

        } catch (IOException ex) {
            System.err.println("Could not play");
        } catch (InterruptedException ex) {
            System.err.println("Error" + ex);
        } finally {
        }
        return true;

    }

    public static boolean playPause() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", "tell application \"Spotify\" to playpause"};
            Process process = runtime.exec(args);
            process.waitFor();

        } catch (IOException ex) {
            System.err.println("Could not playpause");
        } catch (InterruptedException ex) {
            System.err.println("Error" + ex);
        } finally {
        }
        return true;

    }

    public static boolean playTrackWithId(String id, String context) {
        try {
            String query = "tell application \"Spotify\"\n"
                    + "	play track \"spotify:track:" + id + "\"\n"
                    //+ "	play track \"" + context + "\"\n"
                    + "end tell";
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", query};
            Process process = runtime.exec(args);
            process.waitFor();
        } catch (IOException ex) {
            System.err.println("Could not play track");
        } catch (InterruptedException e) {
            System.err.println("Error" + e);
        } finally {
        }
        return true;
    }

    public static SpotofyClientCurrentTrackInfo nextTrack() {
        try {
            
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", "tell application \"Spotify\" to next track"};
            Process process = runtime.exec(args);
            process.waitFor();

        } catch (IOException ex) {
            System.err.println("Could not nextTrack");
        } catch (InterruptedException ex) {
            System.err.println("Error" + ex);
        } finally {
        }
        return getCurrentTrack();
    }

    public static SpotofyClientCurrentTrackInfo prevTrack() {
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", "tell application \"Spotify\" to previous track"};
            Process process = runtime.exec(args);
            process.waitFor();

        } catch (IOException ex) {
            System.err.println("Could not prevTrack");
        } catch (InterruptedException ex) {
            System.err.println("Error" + ex);
        } finally {
        }
        return getCurrentTrack();
    }

    public static SpotofyClientCurrentTrackInfo getCurrentTrack() {

        String query = "tell application \"Spotify\"\n"
                + "	set cstate to \"{\"\n"
                + "	set cstate to cstate & \"\\\"track_id\\\": \\\"\" & current track's id & \"\\\"\"\n"
                + "	set cstate to cstate & \",\\\"position\\\": \" & (player position as integer)\n"
                + "	set cstate to cstate & \",\\\"duration\\\": \" & current track's duration\n"
                + "	set cstate to cstate & \",\\\"state\\\": \\\"\" & player state & \"\\\"\"\n"
                + "	set cstate to cstate & \",\\\"volume\\\": \\\"\" & sound volume & \"\\\"\"\n"
                + "	set cstate to cstate & \"}\n"
                + "\"\n"
                + "	\n"
                + "	return cstate\n"
                + "end tell";

        SpotofyClientCurrentTrackInfo info = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", query};
            Process process = runtime.exec(args);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String resultJson = reader.readLine();
            //System.out.println("Result; " + resultJson);
            info = new SpotofyClientCurrentTrackInfo(resultJson);
            //System.out.println("Volume: " + info.getVolume());
        } catch (IOException ex) {
            System.err.println("Could not findTrack");
        } finally {
        }

        return info;
    }

    public static boolean toggleShuffle() {

        String query = "tell application \"Spotify\"\n"
                + "	set shuffling to not shuffling\n"
                + "	return shuffling\n"
                + "end tell";

        boolean result = false;
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", query};
            Process process = runtime.exec(args);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String resultJson = reader.readLine();
            System.out.println("Result; " + resultJson);
            result = Boolean.valueOf(resultJson);

        } catch (IOException ex) {
            System.err.println("Could not findTrack");
        } finally {
        }

        return result;

    }

    public static int setVolume(int volume) {
        String query = "tell application \"Spotify\"\n"
                + "	set sound volume to " + volume + "\n"
                + "	return sound volume\n"
                + "end tell";
        int result = 0;
        try {
            Runtime runtime = Runtime.getRuntime();
            String[] args = {"osascript", "-e", query};
            Process process = runtime.exec(args);

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String resultJson = reader.readLine();
            System.out.println("Result: " + Integer.parseInt(resultJson));
            result = Integer.parseInt(resultJson);

        } catch (IOException ex) {
            System.err.println("Could not findTrack");
        } finally {
        }
        return result;
    }

}

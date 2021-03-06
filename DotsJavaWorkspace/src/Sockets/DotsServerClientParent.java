package Sockets;

import AndroidCallback.DotsAndroidCallback;
import Dots.DotsPowerUp;
import Dots.DotsPowerUpState;
import Dots.DotsPowerUpType;
import Model.Interaction.DotsInteraction;
import Model.Messages.DotsMessagePowerUp;

import java.io.IOException;

/**
 * Parent class of Dots Server and Client, so that code duplication is eliminated.
 *
 * Primarily for instantiating of the server and client, and to throw an exception if no listeners are set for the server
 * and client
 *
 * Created by JiaHao on 31/3/15.
 */
public abstract class DotsServerClientParent {

    private DotsAndroidCallback androidCallback;

    private boolean gameStarted = false;

    /**
     * Compulsory method to set a listener to update the screen
     */
    public void setAndroidCallback(DotsAndroidCallback androidCallback) {
        this.androidCallback = androidCallback;
    }

    public DotsAndroidCallback getAndroidCallback() {
        return androidCallback;
    }

    /**
     * Call start to start the game and start threads to listen for messages
     */
    public void start() throws IOException, InterruptedException, InstantiationException {

        if (this.androidCallback == null) {
            System.err.println("Listener not set up, exiting");
            throw new InstantiationException();
        }



    }

    /**
     * Override this in the subclass
     * @param interaction
     * @throws IOException
     * @throws InterruptedException
     */
    public void doInteraction (DotsInteraction interaction) throws IOException, InterruptedException {
        System.err.println("Do interaction is not overwrote in parent class!");
    }

    /**
     * Manual method to trigger a screen update
     */
    public void updateBoard() throws IOException {

    }

    /**
     * Used in the android to determine if the game is started to cancel touches
     * @return
     */
    public boolean isGameStarted() {
        return gameStarted;
    }

    public void setGameStarted(boolean gameStarted) {
        this.gameStarted = gameStarted;
    }

    /**
     * Call this method to stop the game from running, and terminate threads
     */
    public void stopGame() {

    }

    /**
     * Static helper method to determine who the winner is
     * @param score
     * @return index of winning player from score array
     */
    public static int getWinner(int[] score) {

        int winningPlayer;

        if (score[0] > score[1]) {
            winningPlayer = 0;
        } else if (score[0] < score[1]) {
            winningPlayer = 1;
        } else {
            // Draw
            winningPlayer = -1;
        }

        return winningPlayer;
    }

    protected void updateLocalPowerUpCallback(DotsMessagePowerUp message) {

        // start powerup
        final DotsPowerUpType powerUpType = message.getPowerUp();

        DotsPowerUp powerUpStart = new DotsPowerUp(powerUpType, DotsPowerUpState.STARTED);
        this.getAndroidCallback().onPowerUpReceived(powerUpStart);

        DotsPowerUpTimedTask delayedTask = new DotsPowerUpTimedTask(powerUpType, message.getDuration(), this.getAndroidCallback());
        delayedTask.start();

    }
}

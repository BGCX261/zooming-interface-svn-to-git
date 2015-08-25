package storage;

/**
 * Saves and loads snapshots of zooming world.
 * <p/>
 * Author: www
 */
public interface ILogger {
    /**
     * Switches writer to recording mode when it tracks dynamic status of zooming world.
     */
    public void startRecording();

    /**
     * Writes collected dump to XML file.
     *
     * @param filename XML file name to write occured events to.
     */
    public void writeXML(String filename);

    /**
     * Stops recording.
     */
    public void stopRecording();
}
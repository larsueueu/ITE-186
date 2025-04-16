import java.awt.*;
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

public class JavaGUIMusicPlayer {
    private Clip clip;
    private long clipTimePosition = 0;
    private JFrame frame;
    private JButton pauseResumeButton;
    private boolean isPaused = false;
    
    public JavaGUIMusicPlayer() {

        frame = new JFrame("WAV Player");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 150);
        frame.setLayout(new FlowLayout());
        frame.setLocationRelativeTo(null);

        ImageIcon icon = new ImageIcon("headphones.png");
        frame.setIconImage(icon.getImage());

        JButton chooseButton = new JButton("Choose File");
        chooseButton.addActionListener(e -> chooseFile());
        frame.add(chooseButton);

        JButton playButton = new JButton("Play");
        playButton.addActionListener(e -> playAudio());
        frame.add(playButton);

        pauseResumeButton = new JButton("Pause");
        pauseResumeButton.addActionListener(e -> pauseOrResumeAudio());
        frame.add(pauseResumeButton);

        JButton forwardButton = new JButton("Forward");
        forwardButton.setBackground(new Color(220, 20, 60));
        forwardButton.setForeground(Color.WHITE);
        forwardButton.addActionListener(e -> forwardAudio());
        frame.add(forwardButton);

        frame.setVisible(true);
    }

    private void chooseFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select WAV File");
        int userSelection = fileChooser.showOpenDialog(frame);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            loadAudioFile(selectedFile.getAbsolutePath());
        }
    }

    private void loadAudioFile(String filePath) {
        try {
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.close();
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(new File(filePath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clipTimePosition = 0;

            JOptionPane.showMessageDialog(frame, "Loaded: " + filePath);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(frame, "Error: " + e.getMessage());
        }
    }

    private void playAudio() {
        if (clip != null) {
            clip.setMicrosecondPosition(clipTimePosition);
            clip.start();
        }
    }

    private void pauseOrResumeAudio() {
        if (clip != null) {
            if (clip.isRunning()) {

                clipTimePosition = clip.getMicrosecondPosition();
                clip.stop();
                isPaused = true;
                pauseResumeButton.setText("Resume");
            } else if (isPaused) {

                clip.setMicrosecondPosition(clipTimePosition);
                clip.start();
                isPaused = false;
                pauseResumeButton.setText("Pause");
            }
        }
    }
    

    private void forwardAudio() {
        if (clip != null) {
            long forwardPosition = clip.getMicrosecondPosition() + 5_000_000;
            if (forwardPosition < clip.getMicrosecondLength()) {
                clip.setMicrosecondPosition(forwardPosition);
                clip.start();
            }
        }
    }

    public static void main(String[] args) {
        new JavaGUIMusicPlayer();
    }
}
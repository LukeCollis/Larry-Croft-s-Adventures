package Recorder;

import App.Controller.Controller;
import Domain.Board.Board;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 tests for the Recorder class.
 */

class RecorderTests {
    private Recorder recorder;
    private Board board;
    private Controller controller;
    /**
     * Set up before each test.
     * A new instance of Recorder is created for each test case.
     */
    @BeforeEach
    void setUp() {
        recorder = new Recorder();
    }



}

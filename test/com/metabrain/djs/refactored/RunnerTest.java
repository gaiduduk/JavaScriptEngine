package com.metabrain.djs.refactored;

import com.metabrain.djs.refactored.node.Node;
import com.metabrain.djs.refactored.node.NodeBuilder;
import com.metabrain.djs.refactored.node.NodeType;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

class RunnerTest {

    @Test
    void run() {
        File currentScript = null;
        String sourceCode = null;
        NodeBuilder builder = new NodeBuilder();
        Runner runThread = new Runner();
        try {
            File nodesTestsDir = new File("test/com/metabrain/djs/nodesTests/");
            File[] tests = nodesTestsDir.listFiles();
            if (tests != null) {
                for (File script : tests) {
                    currentScript = script;
                    sourceCode = FileUtils.readFileToString(script, StandardCharsets.UTF_8);
                    Node module = new Parser().parse(sourceCode);
                    runThread.run(module);
                    Node testFunction = builder.set(module).findLocal("test");
                    assertNotNull(testFunction);
                    Node testResult = builder.set(testFunction).getValueNode();
                    assertNotNull(testResult);
                    Node testValue = builder.set(testResult).getValueNode();
                    assertNotNull(testValue);
                    assertEquals(testValue.type, NodeType.BOOL);
                    assertTrue((Boolean) builder.set(testValue).getData().getObject());
                }
            }else {
                fail("tests not found");
            }
        } catch (Exception e) {
            if (currentScript != null)
                System.out.println(currentScript.getAbsolutePath());
            if (sourceCode != null)
                System.out.println(sourceCode);
            e.printStackTrace();
        }
    }
}

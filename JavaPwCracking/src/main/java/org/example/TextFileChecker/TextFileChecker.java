package org.example.TextFileChecker;

import org.example.Controller.MainAppController;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;

public class TextFileChecker {
    private String _methodName;
    private boolean _doneLoading = false;
    private String[] _lines;

    public TextFileChecker(String path, String methodName) {
        this._methodName = methodName;
        parallelLoad(path);
    }

    private void parallelLoad(String path) {
        Thread t = new Thread(() -> {
            System.out.println(_methodName + " loading start");
            LinkedList<String> temp = new LinkedList<>();
            try (BufferedReader br = new BufferedReader(new FileReader(path))) {
                String line;
                while ((line = br.readLine()) != null) {
                    temp.add(line);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(_methodName + " loading done");
            _lines = new String[temp.size()];
            _lines = temp.toArray(_lines);
            _doneLoading = true;
        });
        t.start();
    }

    public void checkAllEntries(MainAppController callBackClass) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                while (!_doneLoading) {
                    try {
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                for (String curr : _lines) {
                    if(!callBackClass.guessPlain(curr, _methodName))
                    {
                        return;
                    }
                }
            }
        };
        Thread t = new Thread(r);
        t.start();
    }
}

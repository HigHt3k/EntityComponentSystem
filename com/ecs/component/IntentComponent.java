package com.ecs.component;

import com.ecs.intent.Intent;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class IntentComponent extends Component {
    private List<Intent> intents = new ArrayList<Intent>();

    @Override
    public void update() {

    }

    public void handle(KeyEvent e) {
        for(Intent i : intents) {
            i.handleIntent(e);
        }
    }

    public void handle(MouseEvent e) {
        for(Intent i : intents) {
            i.handleIntent(e);
        }
    }

    public <T extends Intent> T getIntent(Class<T> intentClass) {
        for(Intent intent : intents) {
            if(intentClass.isAssignableFrom(intent.getClass())) {
                try {
                    return intentClass.cast(intent);
                } catch(ClassCastException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public <T extends Intent> void removeIntent(Class<T> intentClass) {
        for(int i = 0; i < intents.size(); i ++) {
            Intent intent = intents.get(i);
            if(intentClass.isAssignableFrom(intent.getClass())) {
                intents.remove(i);
                return;
            }
        }
    }

    public void addIntent(Intent intent) {
        this.intents.add(intent);
    }
}

package com.AMiMa.cost_accounting.callbacks;

import android.content.Intent;

public interface MoveToAnotherActivity {
    interface fromSections{
        public void moveTo(Class to);
    }

    interface fromProducts{
        public void moveTo(Intent intent);
    }

}

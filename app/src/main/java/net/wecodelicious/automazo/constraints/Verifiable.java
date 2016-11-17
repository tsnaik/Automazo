package net.wecodelicious.automazo.constraints;

import android.content.Context;

/**
 * Created by Ronak R Patel on 28-10-2016.
 */

public interface Verifiable {
    boolean verify(Context context, Object[] params);
}

package net.wecodelicious.automazo.ui;

import android.content.Context;

import net.wecodelicious.automazo.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Bhaskar on 25/09/02016.
 */
public class View_Templates_Card extends Card {

    public View_Templates_Card(Context context) {
        this(context, R.layout.view_templates_card);
    }

    public View_Templates_Card(Context context, int innerLayout) {
        super(context, innerLayout);
        init();
    }

    private void init() {

    }

    @Override
    public void setInnerLayout(int innerLayout) {
        super.setInnerLayout(innerLayout);
    }
}

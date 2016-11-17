package net.wecodelicious.automazo.ui;

import android.content.Context;

import net.wecodelicious.automazo.R;

import it.gmariotti.cardslib.library.internal.Card;

/**
 * Created by Tanmay on 24-Sep-16.
 */
public class View_Recipe_Card extends Card {
    public View_Recipe_Card(Context context) {
        this(context, R.layout.view_recipe_card);
    }

    public View_Recipe_Card(Context context, int innerLayout) {
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

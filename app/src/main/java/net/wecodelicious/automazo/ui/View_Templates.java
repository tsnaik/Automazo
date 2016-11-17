package net.wecodelicious.automazo.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import net.wecodelicious.automazo.R;

import java.util.ArrayList;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.base.BaseCard;
import it.gmariotti.cardslib.library.view.CardListView;

public class View_Templates extends AppCompatActivity {
    CardArrayAdapter mCardArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__templates);

        final ArrayList<Card> cards = new ArrayList<Card>();

        //Create a Card
        Card card = new View_Templates_Card(this);

        //Create a CardHeader
        CardHeader header = new CardHeader(this);
        header.setTitle("Title ");
        header.setPopupMenu(R.menu.menu_card_header, new CardHeader.OnClickCardHeaderPopupMenuListener(){
            @Override
            public void onMenuItemClick(BaseCard card, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit_card_header:
                        Toast.makeText(getBaseContext(),"Edit clicked",Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.remove_card_header:
                        Toast.makeText(getBaseContext(),"Remove Clicked",Toast.LENGTH_SHORT).show();
                        mCardArrayAdapter.remove((Card) card);
                        //cards.remove(0);

                        break;
                }


            }
        });




        //Add Header to card
        card.addCardHeader(header);

        // card.setInnerLayout(R.layout.view_recipe_card);

        cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);
        //cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);
        //cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);cards.add(card);


        mCardArrayAdapter = new CardArrayAdapter(this,cards);

        CardListView listView = (CardListView) findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
    }
    }


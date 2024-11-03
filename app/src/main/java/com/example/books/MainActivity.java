package com.example.books;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView listView;
    ImageButton btnAdd;
    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        dbHelper = new DbHelper(this);
        listView = findViewById(R.id.listView);
        btnAdd = findViewById(R.id.btnAdd);

        fillData();
        updateListView(); // Обновляем список книг

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddActivity.class);
            startActivity(intent);
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, ContentActivity.class);
                Book book = (Book) parent.getAdapter().getItem(position);
                intent.putExtra("key", book);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateListView(); // Обновляем список при возвращении в активность
    }

    // Метод для обновления списка книг
    private void updateListView() {
        List<Book> books = dbHelper.getData();
        ItemAdapter adapter = new ItemAdapter(this, books);
        listView.setAdapter(adapter);
    }

    void fillData() {
        if (dbHelper.getData().isEmpty()) {
            List<Book> books = new ArrayList<>();
            books.add(new Book("Убить пересмешника", "Харпер Ли", 1960, "Классика", "mockingbird.jpg", "Бессмертный роман о детстве в южном городке и кризисе совести, который всколыхнул его."));
            books.add(new Book("1984", "Джордж Оруэлл", 1949, "Антиутопия", "1984.jpg", "Роман о мрачном будущем, где правительство следит за каждым и контролирует мысли."));
            books.add(new Book("Гордость и предубеждение", "Джейн Остин", 1813, "Романтика", "pride_prejudice.jpg", "Классический роман о любовных отношениях остроумной Элизабет Беннет и надменного мистера Дарси."));
            books.add(new Book("Великий Гэтсби", "Ф. Скотт Фицджеральд", 1925, "Классика", "gatsby.jpg", "Роман об американской мечте и трагической истории Джея Гэтсби."));
            books.add(new Book("Гарри Поттер и философский камень", "Дж. К. Роулинг", 1997, "Фэнтези", "harry_potter.jpg", "Мальчик обнаруживает, что он волшебник, и поступает в Школу чародейства и волшебства Хогвартс."));

            for (Book book : books) {
                dbHelper.insertData(book);
            }
        }
    }
}

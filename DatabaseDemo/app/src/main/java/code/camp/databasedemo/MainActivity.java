package code.camp.databasedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ref to buttons and others
    Button btn_add, btn_viewAll;
    EditText customer_name, customer_number;
    Switch sw_activeCustomer;
    ListView lv_customerList;

    ArrayAdapter customerArrayAdapter;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_add = findViewById(R.id.btn_add);
        btn_viewAll = findViewById(R.id.btn_viewAll);
        customer_name = findViewById(R.id.customer_name);
        customer_number = findViewById(R.id.customer_age);
        sw_activeCustomer = findViewById(R.id.sw_active);
        lv_customerList = findViewById(R.id.lv_customerList);

        databaseHelper = new DatabaseHelper(MainActivity.this);

        ShowCustomersOnListView(databaseHelper);


        // button listener
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomerModel customerModel;

            try{
                customerModel = new CustomerModel(-1,customer_name.getText().toString(),
                        Integer.parseInt(customer_number.getText().toString()), sw_activeCustomer.isChecked());
                Toast.makeText(MainActivity.this,customerModel.toString(), Toast.LENGTH_SHORT).show();

            }
            catch (Exception e){
                Toast.makeText(MainActivity.this,"Erron on add", Toast.LENGTH_SHORT).show();
                customerModel = new CustomerModel(-1, "error", 0, false);
            }

            DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
           boolean succes = databaseHelper.addOne(customerModel);
           Toast.makeText(MainActivity.this,"succes = " + succes, Toast.LENGTH_SHORT).show();
           ShowCustomersOnListView(databaseHelper);
            }
        });

        btn_viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper databaseHelper = new DatabaseHelper(MainActivity.this);
                //Toast.makeText(MainActivity.this,everyone.toString(), Toast.LENGTH_SHORT).show();
                ShowCustomersOnListView(databaseHelper);
            }
        });

        lv_customerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                CustomerModel clickedCustomer = (CustomerModel) adapterView.getItemAtPosition(position);
                databaseHelper.DeleteOne(clickedCustomer);
                ShowCustomersOnListView(databaseHelper);
                Toast.makeText(MainActivity.this, "Deleted " + clickedCustomer.getName(),Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void ShowCustomersOnListView(DatabaseHelper databaseHelper) {
        customerArrayAdapter = new ArrayAdapter<CustomerModel>
                (MainActivity.this, android.R.layout.simple_list_item_1, databaseHelper.getEveryone());
        lv_customerList.setAdapter(customerArrayAdapter);
    }
}
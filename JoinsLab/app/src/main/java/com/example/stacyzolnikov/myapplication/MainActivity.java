package com.example.stacyzolnikov.myapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.IdRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView mListView;
    Button mButtonSameCompany;
    Button mButtonBoston;
    Button mAddDataButton;
    Button mSalaryButton;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAddDataButton = (Button) findViewById(R.id.AddDataButton);
        mAddDataButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addData();
            }
        });

//Employees in Same Company

      mButtonSameCompany = (Button)findViewById(R.id.EmployeesWorkingSameCompanyButton);
        mButtonSameCompany.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mListView = (ListView) findViewById(R.id.listview);
                Cursor cursor = DatabaseHelper.getInstance(view.getContext()).getEmployeesFromSameCompany();
                CursorAdapter adapter = new CursorAdapter(MainActivity.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER){

                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        //textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_FIRST_NAME))+cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_LAST_NAME)));

                        String firstName = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_FIRST_NAME));
                        String lastName = cursor.getString(cursor.getColumnIndex (DatabaseHelper.COL_LAST_NAME));
                        String fullName = firstName + " " + lastName;
                        textView.setText(fullName);
                    }
                };
               mListView.setAdapter(adapter);
                // if (cursor.moveToFirst()){
              //  while(!cursor.isAfterLast()){
                //    result += cursor.getString(cursor.getColumnIndex(COL_COMPANY)) + ".";
                  //  cursor.moveToNext();
               // }
            //}
            }



        });
        mButtonBoston = (Button) findViewById(R.id.CompaniesInBostonButton);
        mButtonBoston.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                mListView = (ListView) findViewById(R.id.listview);
                Cursor cursor = DatabaseHelper.getInstance(view.getContext()).getCompaniesInBoston();
                CursorAdapter adapter = new CursorAdapter(MainActivity.this, cursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER) {
                    @Override
                    public View newView(Context context, Cursor cursor, ViewGroup parent) {
                        return LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1,parent,false);
                    }

                    @Override
                    public void bindView(View view, Context context, Cursor cursor) {
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);
                        textView.setText(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_COMPANY)));
                    }
                };

                mListView.setAdapter(adapter);

            }
        });

//Company with highest salary
        mSalaryButton = (Button) findViewById(R.id.CompanyWithHighestSalaryButton);
        mSalaryButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                mTextView = (TextView) findViewById(R.id.textview);
                DatabaseHelper helper = DatabaseHelper.getInstance(MainActivity.this);
                Cursor cursor = helper.highestSalary();
                      if (cursor.moveToFirst()) {
                          mTextView.setText("Company with the higest Salary: " + cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_COMPANY)));
                          }
                      }
        });


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                final View dialogView = LayoutInflater.from(MainActivity.this).inflate(R.layout.customer_dialog,null);
                builder.setView(dialogView);
                builder.setTitle("Enter Your Employee Info");
                final Employee newEmployee = new Employee();
                final EditText ssn = (EditText) dialogView.findViewById(R.id.SSNTextEdit);
                final EditText firstName = (EditText) dialogView.findViewById(R.id.FirstNameTextEdit);
                final EditText lastName = (EditText) dialogView.findViewById(R.id.LastNameTextEdit);
                final EditText birthYear = (EditText) dialogView.findViewById(R.id.BirthYearTextEdit);
                final EditText city = (EditText) dialogView.findViewById(R.id.CityTextEdit);
                builder.setPositiveButton("Enter Data", null).setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                final AlertDialog dialog = builder.create();
                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                        if (ssn.getText().toString().trim().length() == 0) {
                            ssn.setError("You need to enter a SSN!");
                        }
                        else if (firstName.getText().toString().trim().length()==0){
                            firstName.setError("You need to enter a First Name!");
                        }
                        else if (lastName.getText().toString().trim().length() ==0){
                            lastName.setError("You need to enter a Last Name");
                        }
                        else if (birthYear.getText().toString().trim().length()==0){
                            birthYear.setError("You need to enter a Birth Year");
                        }
                        else if (city.getText().toString().trim().length()==0){
                            city.setError("You need to enter a City!");
                        }
                        else {
                            newEmployee.setSsn(ssn.getText().toString());
                            newEmployee.setFirstName(firstName.getText().toString());
                            newEmployee.setLastName(lastName.getText().toString());
                            newEmployee.setYear(birthYear.getText().toString());
                            newEmployee.setCity(city.getText().toString());
                            DatabaseHelper.getInstance(view.getContext()).insertRowEmployee(newEmployee);
                            Toast.makeText(MainActivity.this, "The employee has been added!", Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }
                    }
                });


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    public void addData() {
        DatabaseHelper helper = DatabaseHelper.getInstance(MainActivity.this);
        //helper.emptyTables();
        Employee employee = new Employee("John", "Smith", "NY", "1973", "123-04-5678");
        Employee employee1 = new Employee("David", "McWill", "Seattle", "1982", "123-04-5679");
        Employee employee2 = new Employee("Katerina", "Wise", "Boston", "1973", "123-04-5680");
        Employee employee3 = new Employee("Donald", "Lee", "London", "1992", "123-04-5681");
        Employee employee4 = new Employee("Gary", "Henwood", "Las Vegas", "1987", "123-04-5682");
        Employee employee5 = new Employee("Anthony", "Bright", "Seattle", "1963", "123-04-5683");
        Employee employee6 = new Employee("William", "Newey", "Boston", "1995", "123-04-5684");
        Employee employee7 = new Employee("Melony", "Smith", "Chicago", "1970", "123-04-5685");

        Job job = new Job("123-04-5678", "Fuzz", "60", "1");
        Job job1 = new Job("123-04-5679", "GA", "70", "2");
        Job job2 = new Job("123-04-5680", "Little Place", "120", "5");
        Job job3 = new Job("123-04-5681", "Macy's", "78", "3");
        Job job4 = new Job("123-04-5682", "New Life", "65", "1");
        Job job5 = new Job("123-04-5683", "Believe", "158", "6");
        Job job6 = new Job("123-04-5684", "Macy's", "200", "8");
        Job job7 = new Job("123-04-5685", "Stop", "299", "12");

        helper.insertRowEmployee(employee);
        helper.insertRowEmployee(employee1);
        helper.insertRowEmployee(employee2);
        helper.insertRowEmployee(employee3);
        helper.insertRowEmployee(employee4);
        helper.insertRowEmployee(employee5);
        helper.insertRowEmployee(employee6);
        helper.insertRowEmployee(employee7);

        helper.insertRowJob(job);
        helper.insertRowJob(job1);
        helper.insertRowJob(job2);
        helper.insertRowJob(job3);
        helper.insertRowJob(job4);
        helper.insertRowJob(job5);
        helper.insertRowJob(job6);
        helper.insertRowJob(job7);

        Toast.makeText(MainActivity.this, "You added data", Toast.LENGTH_SHORT).show();
    }
}

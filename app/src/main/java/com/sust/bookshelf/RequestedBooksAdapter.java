package com.sust.bookshelf;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class RequestedBooksAdapter extends RecyclerView.Adapter<RequestedBooksAdapter.RequestedBooksHolder> {

    public interface OnItemClickListener {
        void onItemClick(Request request);
    }

    private Context context;
    private ArrayList<Request> requestArrayList;
    private FirebaseDatabase database;
    private final RequestedBooksAdapter.OnItemClickListener listener;


    public RequestedBooksAdapter(Context context, ArrayList<Request> requestArrayList,OnItemClickListener listener,FirebaseDatabase database) {
        this.context = context;
        this.requestArrayList = requestArrayList;
        this.database = database;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RequestedBooksHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).
                inflate(R.layout.requestedbooks_recyclerview,viewGroup,false);
        return new RequestedBooksAdapter.RequestedBooksHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull RequestedBooksHolder requestedBooksHolder, int i) {
        requestedBooksHolder.setDetails(requestArrayList.get(i));
        requestedBooksHolder.bind(requestArrayList.get(i),listener);
        requestedBooksHolder.setVisibility(requestArrayList.get(i));
    }

    @Override
    public int getItemCount() {
        return requestArrayList.size();
    }

    public class RequestedBooksHolder extends RecyclerView.ViewHolder {

        private TextView requestedUser, requestedBook, status;
        final static private long ONE_SECOND = 10000;
        private static final int uniqueID= 45612;
        private Button clicked;
        private TextView dateView;
        private int year, month, day;



        final static private long AlertDay = ONE_SECOND * 86400;
        private static final int DAILY_REMINDER_REQUEST_CODE = 100;

        PendingIntent pi;

        BroadcastReceiver br;

        AlarmManager am;


        private TextView returnTextView;
        private EditText returnEditText;

        public RequestedBooksHolder(@NonNull View itemView) {
            super(itemView);
            requestedUser = itemView.findViewById(R.id.requestedUser);
            requestedBook = itemView.findViewById(R.id.requestedBook);
            status = itemView.findViewById(R.id.statusid);
            returnTextView = itemView.findViewById(R.id.returnTextView);
            returnEditText = itemView.findViewById(R.id.returnEditText);
            setup();
        }

        public void setDetails(Request request) {
            requestedUser.setText(request.getUsername());
            requestedBook.setText(request.getBookTitle());

            if(request.getStatus()==0){
                status.setText("pending");
                status.setTextColor(ContextCompat.getColor(context,android.R.color.holo_red_dark));
            }
            else if(request.getStatus()==1){
                status.setTextColor(ContextCompat.getColor(context,android.R.color.holo_green_dark));
                status.setText("approved");
                showNotification(context,context.getClass(),"BookShelf","Book Request Approved!");

            }
            else if(request.getStatus()==2) {
                status.setText("confirm sent");
                status.setTextColor(ContextCompat.getColor(context,android.R.color.holo_blue_dark));
                showNotification(context,context.getClass(),"BookShelf","Book Confirm Sent!");

            }
            else if(request.getStatus()==3) {
                status.setText("confirm recieved");
                returnTextView.setVisibility(View.VISIBLE);
                returnTextView.setText(returnTextView.getText().toString() + request.getReturndate());

                status.setTextColor(ContextCompat.getColor(context,android.R.color.holo_purple));
                am.set( AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() +
                        ONE_SECOND, pi );

                SimpleDateFormat myFormat = new SimpleDateFormat("dd MM yyyy");

                Date c = Calendar.getInstance().getTime();
                System.out.println("Current time => " + c);

                SimpleDateFormat df = new SimpleDateFormat("dd MM yyyy");
                String formattedDate = df.format(c);

                String inputString1 = formattedDate;
                String inputString2 = request.getReturndate();

                try {
                    Date date1 = myFormat.parse(inputString1);
                    Date date2 = myFormat.parse(inputString2);
                    long diff = date2.getTime() - date1.getTime();
                    showNotification(context,context.getClass(),"BookShelf","Book to return within "+diff+" days!");

                    System.out.println ("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        }

        private void setup() {
            br = new BroadcastReceiver() {

                @Override

                public void onReceive(Context context, Intent intent) {
               }

            };
            context.registerReceiver(br, new IntentFilter("com.authorwjf.wakeywakey") );
            pi = PendingIntent.getBroadcast( context, 0, new Intent("com.authorwjf.wakeywakey"),
                    0 );
            am = (AlarmManager)(context.getSystemService( Context.ALARM_SERVICE ));
        }
        private DatePickerDialog.OnDateSetListener myDateListener = new
                DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker arg0,
                                          int arg1, int arg2, int arg3) {
                        showDate(arg1, arg2+1, arg3);
                    }
                };

        private void showDate(int year, int month, int day) {
            dateView.setText(new StringBuilder().append(day).append("/")
                    .append(month).append("/").append(year));
        }
        public void showNotification(Context context,Class<?> cls,String title,String content)
        {
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Intent notificationIntent = new Intent(context, cls);
            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(cls);
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent pendingIntent = stackBuilder.getPendingIntent(
                    DAILY_REMINDER_REQUEST_CODE,PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
            Notification notification = builder.setContentTitle(title)
                    .setContentText(content).setAutoCancel(true)
                    .setSound(alarmSound).setSmallIcon(R.mipmap.ic_launcher_round)
                    .setContentIntent(pendingIntent).build();
            NotificationManager notificationManager = (NotificationManager)
                    context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(DAILY_REMINDER_REQUEST_CODE, notification);
        }


        public void bind(final Request request, OnItemClickListener listener) {
            itemView.findViewById(R.id.cancelBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestArrayList.remove(request);
                    notifyItemRemoved(getAdapterPosition());
                    DatabaseReference db = database.getReference("Profile/" + request.getUsername()
                            + "/booklist/" + request.getParent()
                            + "/requests/" + LoginActivity.user);
                    db.setValue(null); //0 for pending,1 for approved,2 for confirmed

                    db = database.getReference("Profile/" + LoginActivity.user
                            + "/requestedBooks/" + request.getParent());
                    db.setValue(null); //0 for pending,1 for approved,2 for confirmed


                }
            });

            itemView.findViewById(R.id.confirmBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference databaseReference = database.getReference("Profile/" + request.getUsername() +
                            "/booklist/" + request.getParent() + "/requests/" + LoginActivity.user);
                    databaseReference.child("status").setValue(3);

                    databaseReference = database.getReference("Profile/" + LoginActivity.user + "/requestedBooks/" +
                            request.getParent());
                    databaseReference.child("status").setValue(3);

                    databaseReference = database.getReference("Profile/"+request.getUsername()+"/booklist/"+
                                        request.getParent());
                    databaseReference.child("availability").setValue(0);
                    databaseReference = database.getReference("Books/"+request.getParent()+"/users/"+request.getUsername());
                    databaseReference.child("availability").setValue(0);

                    request.setStatus(3);
                    setVisibility(request);
                    setDetails(request);

                    databaseReference = database.getReference("Profile/"+LoginActivity.user+"/borrowedBooks/"+
                                                                    request.getParent());
                    databaseReference.child("username").setValue(request.getUsername());
                    databaseReference.child("parent").setValue(request.getParent());
                    databaseReference.child("bookTitle").setValue(request.getBookTitle());

                    databaseReference = database.getReference("Profile/"+request.getUsername()+"/lentBooks/"+
                                                                request.getParent());
                    databaseReference.child("username").setValue(LoginActivity.user);
                    databaseReference.child("parent").setValue(request.getParent());
                    databaseReference.child("bookTitle").setValue(request.getBookTitle());
                }
            });

        }
        public void setVisibility(Request request){
            if(request.getStatus()==2){
                itemView.findViewById(R.id.confirmBtn).setVisibility(View.VISIBLE);
            }
            if(request.getStatus()==3){
                itemView.findViewById(R.id.cancelBtn).setVisibility(View.GONE);
                itemView.findViewById(R.id.confirmBtn).setVisibility(View.GONE);
            }
        }
    }


    }

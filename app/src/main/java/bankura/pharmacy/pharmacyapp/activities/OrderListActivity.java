package bankura.pharmacy.pharmacyapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;

import bankura.pharmacy.pharmacyapp.App;
import bankura.pharmacy.pharmacyapp.R;
import bankura.pharmacy.pharmacyapp.adapters.OrdersAdapter;
import bankura.pharmacy.pharmacyapp.controllers.OrderManager;
import bankura.pharmacy.pharmacyapp.models.Order;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jp.wasabeef.recyclerview.animators.SlideInRightAnimator;

/**
 * An activity representing a list of Orders. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link OrderDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class OrderListActivity extends AppCompatActivity implements OrdersAdapter.OnOrderClickListener{

    public final String TAG = this.getClass().getSimpleName();
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Check if user logged in else go back to login activity
        if (App.getFirebase().getAuth() == null) {
            startActivity(LoginActivity.getInstance(this));
            finish();
            return;
        }

        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              //  startActivity(NewOrderActivity.getInstance(OrderListActivity.this));

                BottomSheetDialogFragment bottomSheetDialogFragment = new NewOrderFragment();
                bottomSheetDialogFragment.show(getSupportFragmentManager(), bottomSheetDialogFragment.getTag());

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.order_list);
        assert recyclerView != null;
       // setupRecyclerView((RecyclerView) recyclerView);
        OrdersAdapter adapter = new OrdersAdapter(recyclerView, this);


        // Set animator
        SlideInRightAnimator slideAnimator = new SlideInRightAnimator(new OvershootInterpolator(0.5f));
        slideAnimator.setAddDuration(300);
        slideAnimator.setRemoveDuration(300);
        slideAnimator.setChangeDuration(300);
        slideAnimator.setMoveDuration(300);

       // recyclerView.setItemAnimator(slideAnimator);
//        SlideInOutRightItemAnimator animator = new SlideInOutRightItemAnimator(recyclerView);
//        animator.setAddDuration(400);
//        animator.setChangeDuration(400);
        recyclerView.setItemAnimator(slideAnimator);
        recyclerView.setAdapter(adapter);

        if (findViewById(R.id.order_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
    }

    @Override
    public void onOrderClick(String orderId) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putString(OrderDetailFragment.ORDER_ID, orderId);
            OrderDetailFragment fragment = new OrderDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.order_detail_container, fragment)
                    .commit();
        } else {

            startActivity(OrderDetailActivity.getInstance(this, orderId));

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_order_list, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(EditUserActivity.getInstance(this));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /* private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(DummyContent.ITEMS));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<DummyContent.DummyItem> mValues;

        public SimpleItemRecyclerViewAdapter(List<DummyContent.DummyItem> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.order_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
            holder.mIdView.setText(mValues.get(position).id);
            holder.mContentView.setText(mValues.get(position).content);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putString(OrderDetailFragment.ARG_ITEM_ID, holder.mItem.id);
                        OrderDetailFragment fragment = new OrderDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.order_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, OrderDetailActivity.class);
                        intent.putExtra(OrderDetailFragment.ARG_ITEM_ID, holder.mItem.id);

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mContentView;
            public DummyContent.DummyItem mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mContentView = (TextView) view.findViewById(R.id.content);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mContentView.getText() + "'";
            }
        }
    }*/

    public static Intent getInstance(Context context) {
        return new Intent(context, OrderListActivity.class);
    }

    @OnClick(R.id.button_create_order)
    void create_order() {
        Log.d(TAG, "creating order");
        String uid = App.getFirebase().getAuth().getUid();
        Order order = new Order();
        long timestamp = System.currentTimeMillis() / 1000L;
        order.setUid(uid);
        order.setCreatedAt(timestamp);
        order.setPrice(3434.34);
        order.setShippingCharge(34.23);
        order.setCompleted(false);
        order.setStatus(Order.Status.CANCELED);
        order.setNote("this is a note");

        String key = OrderManager.createOrder(order);

        Log.d(TAG, "create_order: new key: " + key);

       // OrderManager.setCompleted(key);
//        Firebase ref = App.getFirebase();
//
//       Firebase newOrderRef =  ref.child("orders").push();
//        newOrderRef.setValue(order);
//        ref.child("order_stats").child("open").child(newOrderRef.getKey()).setValue(true);
    }


}

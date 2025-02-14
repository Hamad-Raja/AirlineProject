package com.example.airlineproject;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class ticketFragment extends Fragment {

    private static final String ARG_FLIGHT_ID = "flight_id";
    private int flightId;
    private TextView ticketDetails;

    public ticketFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of this fragment.
     * @param flightId The flight ID to display in the ticket.
     * @return A new instance of ticketFragment.
     */
    public static ticketFragment newInstance(int flightId) {
        ticketFragment fragment = new ticketFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_FLIGHT_ID, flightId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            flightId = getArguments().getInt(ARG_FLIGHT_ID, -1); // Default to -1 if not found
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout
        View view = inflater.inflate(R.layout.fragment_ticket, container, false);

        // Initialize TextView
        ticketDetails = view.findViewById(R.id.ticketDetails);

        // Display flight ID
        if (flightId != -1) {
            ticketDetails.setText("Your ticket for flight ID: " + flightId + " is confirmed.");
        } else {
            ticketDetails.setText("No flight ID provided.");
        }

        return view;
    }
}

package tdt4140.gui;

import tdt4140.calendarsystem.Appointment;
import tdt4140.calendarsystem.Participant;
import tdt4140.calendarsystem.User;
import tdt4140.calendarsystem.UserManager;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by trond on 3/21/14.
 */
public class UserListPanel extends JPanel {


    private class UserEntry
    {
        public User user;
        public JCheckBox checkBox;
    }

    private ArrayList<UserEntry> userEntries;
    private ArrayList<User> activeUsers;

    public UserListPanel(JPanel contentPanel)
    {
        super(null);
        final int USERS_LEFT_POS = 20;
        //Create and place separate content panel for JLists to add participants
        this.setBounds(USERS_LEFT_POS - 10, 16, 200, 270);
        this.setBorder(BorderFactory
                .createTitledBorder("Included calendars:"));
        this.setBackground(contentPanel.getBackground());
        contentPanel.add(this);

        userEntries = new ArrayList<>();
        activeUsers = new ArrayList<>();




        for (User u : UserManager.getInstance().getUsers())
        {
            final int index = userEntries.size();
            final UserEntry entry = new UserEntry();
            entry.user = u;
            entry.checkBox = new JCheckBox(u.getName(), UserManager.getInstance().getCurrentUser() == u);
            entry.checkBox.setBounds(USERS_LEFT_POS, 20 + index * 20, 200, 20);

            entry.checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    setUserChecked(entry.user, entry.checkBox.isSelected());
                }
            });
            this.add(entry.checkBox);

            userEntries.add(entry);
        }

//        MainFrame.refreshAppoint();
    }

    private void setUserChecked(User u, boolean checked)
    {
        if (checked && !activeUsers.contains(u))
            activeUsers.add(u);
        if (!checked && activeUsers.contains(u))
            activeUsers.remove(u);

        MainFrame.refreshAppoint();
    }

    public ArrayList<User> getActiveUsers() {return activeUsers; }

    public boolean isActive(Appointment appointment)
    {
        for (Participant p : appointment.getParticipants())
            if (activeUsers.contains(p.getaUser()) && !p.getStatus().equals(Participant.STATUS_HIDDEN))
                return true;

        return false;
    }

}

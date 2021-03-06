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
public class AppointmentStatusPanel extends JPanel {

    private Appointment mAppointment;
    private JRadioButton rbAccept, rbDecline, rbNoAnswer, rbHide;
    private User mUser;

    private class StatusEntry
    {
        public User user;
        public String status;
    }

    private ArrayList<StatusEntry> statusEntries;

    public AppointmentStatusPanel(JPanel contentPanel, Appointment appointment)
    {
        super(null);
        if (appointment != mAppointment)
            statusEntries = new ArrayList<>();
        mAppointment = appointment;

        final int STATUS_LEFT_POS = 10;
        //Create and place separate content panel for JLists to add participants
        this.setBounds(330, 186, 230, 120);
        this.setBorder(BorderFactory
                .createTitledBorder("Set status for " + UserManager.getInstance().getCurrentUser().getName()));
        this.setBackground(contentPanel.getBackground());
        contentPanel.add(this);


        rbNoAnswer = new JRadioButton(Participant.STATUS_NOT_RESPONDED);
        rbNoAnswer.setBounds(STATUS_LEFT_POS, 20, 217, 23);
        this.add(rbNoAnswer);
        rbNoAnswer.setSelected(true);

        rbDecline = new JRadioButton(Participant.STATUS_DECLINED);
        rbDecline.setBounds(STATUS_LEFT_POS, 40, 217, 23);
        this.add(rbDecline);


        rbAccept = new JRadioButton(Participant.STATUS_ATTENDING);
        rbAccept.setBounds(STATUS_LEFT_POS, 60, 217, 23);
        this.add(rbAccept);


        rbHide = new JRadioButton(Participant.STATUS_HIDDEN);
        rbHide.setBounds(STATUS_LEFT_POS, 80, 217, 23);
        this.add(rbHide);

        ButtonGroup statusGroup = new ButtonGroup();
        statusGroup.add(rbNoAnswer);
        statusGroup.add(rbDecline);
        statusGroup.add(rbAccept);
        statusGroup.add(rbHide);

        rbNoAnswer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {setStatus(Participant.STATUS_NOT_RESPONDED);
            }
        });

        rbDecline.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setStatus(Participant.STATUS_DECLINED);
            }
        });

        rbAccept.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setStatus(Participant.STATUS_ATTENDING);
            }
        });

        rbHide.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setStatus(Participant.STATUS_HIDDEN);
            }
        });

        setUser(UserManager.getInstance().getCurrentUser());

    }

    private void setStatus(String status)
    {
        if (mUser == null)
            return;

        for (StatusEntry entry : statusEntries)
        {
            if (entry.user == mUser)
            {
                entry.status = status;
                return;
            }
        }
        StatusEntry newEntry = new StatusEntry();
        newEntry.user = mUser;
        newEntry.status = status;

        statusEntries.add(newEntry);
    }

    public void apply()
    {
        for (int i = 0; i < statusEntries.size(); i++)
            mAppointment.setStatus(statusEntries.get(i).user, statusEntries.get(i).status);
    }

    private boolean hasEntry(User u)
    {
        for (StatusEntry entry : statusEntries)
        {
            if (entry.user == mUser)
                return true;
        }
        return false;
    }

    private String getStatus(User u)
    {
        for (StatusEntry entry : statusEntries)
        {
            if (entry.user == mUser)
                return entry.status;
        }
        return null;
    }

    public void setUser(User user)
    {
        mUser = user;
        if (user == null || (mAppointment.getParticipant(user) != null
                && mAppointment.getParticipant(user).getStatus().equals(Participant.STATUS_CREATOR)))
        {
            rbAccept.setEnabled(false);
            rbDecline.setEnabled(false);
            rbNoAnswer.setEnabled(false);
            rbHide.setEnabled(false);

            this.setBorder(BorderFactory
                    .createTitledBorder("Status not changeable"));
        }
        else
        {
            rbAccept.setEnabled(true);
            rbDecline.setEnabled(true);
            rbNoAnswer.setEnabled(true);
            rbHide.setEnabled(true);

            rbAccept.setSelected(false);
            rbDecline.setSelected(false);
            rbNoAnswer.setSelected(false);
            rbHide.setSelected(false);

            if (hasEntry(user))
            {
                switch (getStatus(user))
                {
                    case Participant.STATUS_ATTENDING:
                        rbAccept.setSelected(true);
                        break;
                    case Participant.STATUS_DECLINED:
                        rbDecline.setSelected(true);
                        break;
                    case Participant.STATUS_NOT_RESPONDED:
                        rbNoAnswer.setSelected(true);
                        break;
                    case Participant.STATUS_HIDDEN:
                        rbHide.setSelected(true);
                        break;

                }

            }
            else if (mAppointment.getParticipant(mUser) != null)
            {
                switch (mAppointment.getParticipant(user).getStatus())
                {
                    case Participant.STATUS_ATTENDING:
                        rbAccept.setSelected(true);
                        break;
                    case Participant.STATUS_DECLINED:
                        rbDecline.setSelected(true);
                        break;
                    case Participant.STATUS_NOT_RESPONDED:
                        rbNoAnswer.setSelected(true);
                        break;
                    case Participant.STATUS_HIDDEN:
                        rbHide.setSelected(true);
                        break;
                }
            }
            else
            {
                rbNoAnswer.setSelected(true);
            }


            this.setBorder(BorderFactory
                    .createTitledBorder("Set status for " + mUser.getName()));
        }


    }

}

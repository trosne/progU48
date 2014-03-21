package tdt4140.gui;

import tdt4140.calendarsystem.Appointment;
import tdt4140.calendarsystem.CalendarManager;
import tdt4140.calendarsystem.UserManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by trond on 3/21/14.
 */
public class AlarmThread extends Thread {

    private static final long MINUTE = 1000*60;
    private MainFrame mf;
    private ArrayList<Appointment> postedAppointments;

    public AlarmThread(MainFrame m)
    {
        this.start();
        this.mf = m;
        postedAppointments = new ArrayList<>();
    }

    @Override
    public void run()
    {
        CalendarManager cm = CalendarManager.getInstance();
        while (true)
        {
            long currTime = System.currentTimeMillis();
            for (Appointment a : cm.getAppointments())
            {
                if (a.getAlarm() == null || a.getParticipant(UserManager.getInstance().getCurrentUser()) == null)
                    continue;
                long alarm = a.getAlarm().getTime();

                if (alarm >= currTime && alarm < currTime + MINUTE && !postedAppointments.contains(a))
                {
                    postAlarm(a);
                    postedAppointments.add(a);
                }

            }

            try {
                synchronized(this) { sleep(MINUTE); }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.out.println("CHECKING ALARMS");
        }
    }

    private void postAlarm(Appointment a)
    {
        final JDialog alarmWindow = new JDialog(mf, a.getDescription(), true);

        alarmWindow.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        alarmWindow.setBounds(300, 100, 300, 300);
        Container pane = alarmWindow.getContentPane();
        pane.setLayout(null);

        final JPanel contentPanel = new JPanel(null);

        pane.add(contentPanel);
        contentPanel.setBounds(0, 0, 290, 290);
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel textAlarm = new JLabel("ALARM!");
        textAlarm.setBounds(10, 10, 290, 100);
        contentPanel.add(textAlarm);

        JLabel text = new JLabel("Appointment " + a.getDescription() + " starts soon.");
        text.setBounds(10, 30, 290, 100);
        contentPanel.add(text);

        JButton dismiss = new JButton("Dismiss");
        dismiss.setBounds(100, 200, 100, 40);
        contentPanel.add(dismiss);

        dismiss.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                alarmWindow.setVisible(false);
            }
        });

        alarmWindow.setVisible(true);

        System.out.println("Posted alarm for " + a.getDescription());
    }
}

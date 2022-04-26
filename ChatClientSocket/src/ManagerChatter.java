import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.EmptyBorder;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.JTabbedPane;

public class ManagerChatter extends JFrame implements Runnable{

	/**
	 * 
	 */
	//private static final long serialVersionUID = 1L;
	/**
	 * 
	 */
	
	private JPanel contentPane;
	private JTextField txtServerPort;

	ServerSocket srvSocket = null;
	BufferedReader bf = null;
	Thread t;
	private JTabbedPane tabbedPane;
	private JTabbedPane tabbedPane_1;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ManagerChatter frame = new ManagerChatter();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ManagerChatter() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 622, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setLayout(new GridLayout(1, 2, 0, 0));
		
		JLabel lblNewLabel = new JLabel("Manager Port: ");
		lblNewLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		panel.add(lblNewLabel);
		
		txtServerPort = new JTextField();
		txtServerPort.setText("29013");
		panel.add(txtServerPort);
		txtServerPort.setColumns(10);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane, BorderLayout.CENTER);
		
		tabbedPane_1 = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("Tab Messages", null, tabbedPane_1, null);
		
		tabbedPane.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent e) {
				if(SwingUtilities.isRightMouseButton(e)) {
					int index = tabbedPane.getSelectedIndex();
					if(index !=0) {
						JPopupMenu popupMenu = new JPopupMenu();
						JMenuItem delete = new JMenuItem("Delete");
						delete.addActionListener(new ActionListener() {

							@Override
							public void actionPerformed(ActionEvent e) {

		                      tabbedPane.remove(index);
		                      
								
							}
							
						});
						popupMenu.add(delete);
						
						popupMenu.show(tabbedPane, e.getX(),e.getY()+40); //note
						
					}
					
				}
				
				
				
				
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		); 
		
		this.setSize(600, 300);
		int serverPort = Integer.parseInt(txtServerPort.getText());
		try {
			srvSocket = new ServerSocket(serverPort);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		t = new Thread(this);
		t.start();
	}

	public void run() {
		while(true) {
			try {
				Socket aStaffSocket = srvSocket.accept();
				if(aStaffSocket != null) {
					bf = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream() ));
					String S = bf.readLine();
					int pos = S.indexOf(":");
					String staffName = S.substring(pos+1);
					ChatPanel p = new ChatPanel(aStaffSocket, "Manager", staffName);
					tabbedPane.add(staffName,p);
					p.updateUI();
				}
				Thread.sleep(100);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
}

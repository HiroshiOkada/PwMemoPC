
package com.toycode.pwmemopc;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.JTextComponent;
import javax.swing.KeyStroke;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class PwMemoPC implements Runnable {

    private JFrame mMainFrame;
    private JList mTitileList;
    private JTextField mTitleTextField;
    private JTextField mIdTextField;
    private JTextField mPasswordTextField;
    private JTextPane mMemoTextPane;
    private JTextComponent mCurrentTextComponent = null;
    private DefaultListModel mTitles = new DefaultListModel();
    private boolean mIsRun;


    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    PwMemoPC window = new PwMemoPC();
                    window.mMainFrame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the application.
     */
    public PwMemoPC() {
        initialize();
    }

    /**
     * Initialize the contents of the mMainFrame.
     */
    private void initialize() {
        mMainFrame = new JFrame();
        mMainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent arg0) {
                mIsRun = false;
            }
        });
        mMainFrame.setBounds(40, 100, 600, 480);

        mMainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JMenuBar menuBar = new JMenuBar();
        mMainFrame.setJMenuBar(menuBar);

        JMenu fileMenu = new JMenu("File");
        fileMenu.setMnemonic(KeyEvent.VK_F);
        menuBar.add(fileMenu);

        JMenuItem openFileMenuItem = new JMenuItem("Open File...");
        openFileMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doOpenFile();
            }
        });
        openFileMenuItem.setMnemonic(KeyEvent.VK_O);
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
        fileMenu.add(openFileMenuItem);
        
        JMenuItem saveMenuItem = new JMenuItem("Save");
        saveMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doSave();
            }
        });
        saveMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
        fileMenu.add(saveMenuItem);

        JMenuItem saveAsMenuItem = new JMenuItem("save As...");
        saveAsMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doSaveAs();
            }
        });
        saveAsMenuItem.setMnemonic(KeyEvent.VK_A);
        fileMenu.add(saveAsMenuItem);

        JMenu editMenu = new JMenu("Edit");
        editMenu.setMnemonic(KeyEvent.VK_E);
        menuBar.add(editMenu);

        JMenuItem cutMenuItem = new JMenuItem("Cut");
        cutMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doCut();
            }
        });
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
        editMenu.add(cutMenuItem);

        JMenuItem copyMenuItem = new JMenuItem("Copy");
        copyMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doCopy();
            }
        });
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, InputEvent.CTRL_MASK));
        editMenu.add(copyMenuItem);

        JMenuItem pasteMenuItem = new JMenuItem("Paste");
        pasteMenuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                doPaste();
            }
        });
        openFileMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK));
        editMenu.add(pasteMenuItem);
        mMainFrame.getContentPane().setLayout(new BorderLayout(0, 0));

        mTitileList = new JList(mTitles);
        mTitileList.setPreferredSize(new Dimension(64, 0));
        mTitileList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mTitileList.addListSelectionListener(mListSelectionListener);

        mMainFrame.getContentPane().add(mTitileList, BorderLayout.WEST);

        JPanel mMainPanel = new JPanel();
        mMainFrame.getContentPane().add(mMainPanel, BorderLayout.CENTER);
        GridBagLayout gbl_mMainPanel = new GridBagLayout();
        gbl_mMainPanel.columnWidths = new int[] {
                70, 440, 0
        };
        gbl_mMainPanel.rowHeights = new int[] {
                19, 19, 19, 346, 0
        };
        gbl_mMainPanel.columnWeights = new double[] {
                0.0, 0.0, Double.MIN_VALUE
        };
        gbl_mMainPanel.rowWeights = new double[] {
                0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE
        };
        mMainPanel.setLayout(gbl_mMainPanel);

        JLabel titleLabel = new JLabel("Title");
        GridBagConstraints gbc_titleLabel = new GridBagConstraints();
        gbc_titleLabel.anchor = GridBagConstraints.EAST;
        gbc_titleLabel.insets = new Insets(0, 0, 5, 5);
        gbc_titleLabel.gridx = 0;
        gbc_titleLabel.gridy = 0;
        mMainPanel.add(titleLabel, gbc_titleLabel);

        mTitleTextField = new JTextField();
        GridBagConstraints gbc_mTitleTextField = new GridBagConstraints();
        gbc_mTitleTextField.anchor = GridBagConstraints.NORTH;
        gbc_mTitleTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_mTitleTextField.insets = new Insets(0, 0, 5, 0);
        gbc_mTitleTextField.gridx = 1;
        gbc_mTitleTextField.gridy = 0;
        mTitleTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                mCurrentTextComponent = mTitleTextField;
            }
            @Override
            public void focusLost(FocusEvent e) {
                if( !e.isTemporary() && (mCurrentTextComponent == mTitleTextField)) {
                    mCurrentTextComponent = null;
                }
            }
        });
        mMainPanel.add(mTitleTextField, gbc_mTitleTextField);
        mTitleTextField.setColumns(10);

        JLabel passwordLabel = new JLabel("User ID");
        GridBagConstraints gbc_passwordLabel = new GridBagConstraints();
        gbc_passwordLabel.anchor = GridBagConstraints.EAST;
        gbc_passwordLabel.insets = new Insets(0, 0, 5, 5);
        gbc_passwordLabel.gridx = 0;
        gbc_passwordLabel.gridy = 1;
        mMainPanel.add(passwordLabel, gbc_passwordLabel);

        mIdTextField = new JTextField();
        GridBagConstraints gbc_mIdTextField = new GridBagConstraints();
        gbc_mIdTextField.anchor = GridBagConstraints.NORTH;
        gbc_mIdTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_mIdTextField.insets = new Insets(0, 0, 5, 0);
        gbc_mIdTextField.gridx = 1;
        gbc_mIdTextField.gridy = 1;
        mIdTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                mCurrentTextComponent = mIdTextField;
            }
            @Override
            public void focusLost(FocusEvent e) {
                if( !e.isTemporary() && (mCurrentTextComponent == mIdTextField)) {
                    mCurrentTextComponent = null;
                }
            }
        });
        mMainPanel.add(mIdTextField, gbc_mIdTextField);
        mIdTextField.setColumns(10);

        JLabel passwordLabel_1 = new JLabel("Password");
        GridBagConstraints gbc_passwordLabel_1 = new GridBagConstraints();
        gbc_passwordLabel_1.anchor = GridBagConstraints.EAST;
        gbc_passwordLabel_1.insets = new Insets(0, 0, 5, 5);
        gbc_passwordLabel_1.gridx = 0;
        gbc_passwordLabel_1.gridy = 2;
        mMainPanel.add(passwordLabel_1, gbc_passwordLabel_1);

        mPasswordTextField = new JTextField();
        GridBagConstraints gbc_mPasswordTextField = new GridBagConstraints();
        gbc_mPasswordTextField.anchor = GridBagConstraints.NORTH;
        gbc_mPasswordTextField.fill = GridBagConstraints.HORIZONTAL;
        gbc_mPasswordTextField.insets = new Insets(0, 0, 5, 0);
        gbc_mPasswordTextField.gridx = 1;
        gbc_mPasswordTextField.gridy = 2;
        mPasswordTextField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                mCurrentTextComponent = mPasswordTextField;
            }
            @Override
            public void focusLost(FocusEvent e) {
                if( !e.isTemporary() && (mCurrentTextComponent == mPasswordTextField)) {
                    mCurrentTextComponent = null;
                }
            }
        });
        mMainPanel.add(mPasswordTextField, gbc_mPasswordTextField);
        mPasswordTextField.setColumns(10);
        
        JLabel memoLabel = new JLabel("New label");
        GridBagConstraints gbc_memoLabel = new GridBagConstraints();
        gbc_memoLabel.anchor = GridBagConstraints.NORTHWEST;
        gbc_memoLabel.insets = new Insets(0, 0, 0, 5);
        gbc_memoLabel.gridx = 0;
        gbc_memoLabel.gridy = 3;
        mMainPanel.add(memoLabel, gbc_memoLabel);

        mMemoTextPane = new JTextPane();
        mMemoTextPane.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent arg0) {
                mCurrentTextComponent = mMemoTextPane;
            }
            @Override
            public void focusLost(FocusEvent e) {
                if( !e.isTemporary() && (mCurrentTextComponent == mMemoTextPane)) {
                    mCurrentTextComponent = null;
                }
            }
        });
        GridBagConstraints gbc_mMemoTextPane = new GridBagConstraints();
        gbc_mMemoTextPane.fill = GridBagConstraints.BOTH;
        gbc_mMemoTextPane.gridx = 1;
        gbc_mMemoTextPane.gridy = 3;
        mMainPanel.add(mMemoTextPane, gbc_mMemoTextPane);
        mTitles.addElement("one");
        mTitles.addElement("Two");

        mIsRun = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        while (mIsRun) {
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    Component c = mMainFrame.getFocusOwner();
                    if (c != null) {
                        //mMainFrame.setTitle(c.toString());
                    }
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    private ListSelectionListener mListSelectionListener = new ListSelectionListener() {
        @Override
        public void valueChanged(ListSelectionEvent event) {
            mTitileList.getSelectedIndex();
            mMemoTextPane.setText(event.toString());
        }
    };

    private void doOpenFile() {
        
    }

    private void doSave() {
        
    }
    
    private void doSaveAs() {
        
    }

    private void doCut() {
         if (mCurrentTextComponent != null) {
             mCurrentTextComponent.cut();
        }
    }

    private void doCopy() {
        if (mCurrentTextComponent != null) {
            mCurrentTextComponent.copy();
       }
    }
    
    private void doPaste() {
        if (mCurrentTextComponent != null) {
            mCurrentTextComponent.paste();
       }        
    }

}

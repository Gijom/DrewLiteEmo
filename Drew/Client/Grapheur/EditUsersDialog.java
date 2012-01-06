package Drew.Client.Grapheur ;

import java.awt.*;
import java.awt.List;
import java.awt.event.*;
import java.util.*;

/**
   Fenetre d'edition des utilisateurs courants et d'introduction de
   nouveaux utilisateurs.
   @version JAVA 1.1 : utilise un ActionListener pour gérer les boutons.
*/
public class EditUsersDialog extends Dialog implements ActionListener{
    /** La fenêtre à laquelle ce Dialog est rattaché. 
	En effet, ce Dialog bloque temporairement toute action dans la 
	fenêtre parente...
    */
    Grapheur parent;

    /** Etiquette figurant le titre de la liste
     */
    Label protagoList;
    
    /** Liste affichant les utilisateurs.
     */    
    List listUsers;
    
    /** Bouton New 
     @see newEntry
    */
    Button nouveauBouton;
    
    /** Bouton Edit  
     @see editEntry
    */    
    Button editBouton;
    
    /** Bouton Delete  
     @see deleteEntry
    */    
    Button deleteBouton;
    
    /** Bouton Close pour fermer le Dialog
    */    
    Button closeBouton;
    
    /** Champ de texte pour le nom  
     */        
    TextField nomTexte;

    /** menu déroulant pour le choix de la couleur de l'utilisateur  
     */     
    Choice choixCouleur;

    /** Case à cocher pour engager (activer) ou non l'utilisateur dans 
	la session
     */     
    Checkbox engagedChbx;
    
    /** Bouton Save
     @see saveEntry
    */        
    Button saveCloseBouton;

    /** Bouton Cancel  
     @see cancelEntry
    */    
    Button cancelBouton;
    
    /** L'utilisateur courant */
    User cu ; 	

    Dimension dim;
    
    /** Constructeur du dialog box.
	Charge le constructeur generique de Dialog avec des parametres
	fixes.
	@params f la fenêtre parente (Grapheur), paralysée durant toute la
	manipulation des utilisateurs.
    */
    public EditUsersDialog(Grapheur f) {
	super(f.getWindowParent(),"Edit", true);
	this.parent = (Grapheur) f;
	setTitle(parent.comment.getString("EUWindowTitle"));
	setSize(200,300);
	setResizable(true);
	setBackground(Color.lightGray);
	
	GridBagLayout gridbag = new GridBagLayout();
	GridBagConstraints c = new GridBagConstraints();
	
	setLayout(gridbag);
	c.fill = GridBagConstraints.BOTH;
	
        listUsers = new List(4,false); //3 visibles par défauts, non redimensionable.	
	listUsers.setBackground(new Color(0xFFFFFF));
	listUsers.setFont(parent.police);
	c.insets = new Insets(6,6,3,6);
	c.gridx = 0;
	c.gridy = 0;
	c.gridwidth = 3;
	c.gridheight = 3;
	c.weightx=1.0; c.weighty = 1.0;
	gridbag.setConstraints(listUsers,c);
        add(listUsers);

	nouveauBouton = new Button(parent.comment.getString("ButtonNew"));
	nouveauBouton.setFont(parent.police);
	c.fill = GridBagConstraints.HORIZONTAL;
	c.insets = new Insets(3,6,3,3);
	c.gridx = 0;
	c.gridy = 3;
	c.gridwidth = 1;
	c.gridheight = 1;
	c.weightx=0.33; c.weighty = 0.0;
	gridbag.setConstraints(nouveauBouton,c);
	nouveauBouton.addActionListener(this);
	nouveauBouton.setActionCommand("New");
        add(nouveauBouton);

	editBouton = new Button(parent.comment.getString("EUButtonEdit"));
	editBouton.setFont(parent.police);
	c.insets = new Insets(3,3,3,3);
	c.gridx = 1;
	c.gridy = 3;
	gridbag.setConstraints(editBouton,c);
	editBouton.addActionListener(this);
	editBouton.setActionCommand("Edit");
        add(editBouton);

	deleteBouton = new Button(parent.comment.getString("ButtonDelete"));
	deleteBouton.setFont(parent.police);
	c.insets = new Insets(3,3,3,6);
	c.gridx = 2;
	c.gridy = 3;
	gridbag.setConstraints(deleteBouton,c);
	deleteBouton.addActionListener(this);
	deleteBouton.setActionCommand("Delete");
        add(deleteBouton);

	nomTexte = new TextField("");
	nomTexte.setBackground(new Color(0xFFFFFF));
	nomTexte.setFont(parent.police);
	c.insets= new Insets(3,6,3,6);
	c.gridx = 0;
	c.gridy = 4;
	c.gridwidth = 3;
	c.gridheight = 1;
	gridbag.setConstraints(nomTexte,c);
        add(nomTexte);

	saveCloseBouton = new Button(parent.comment.getString("ButtonClose"));
	saveCloseBouton.setFont(parent.police);
	saveCloseBouton.addActionListener(this);
	saveCloseBouton.setActionCommand("Close");
	c.insets = new Insets(3,3,6,6);
	c.gridx = 2;
	c.gridy = 6;
	c.gridwidth = 1;
	c.gridheight = 1;
	gridbag.setConstraints(saveCloseBouton,c);
        add(saveCloseBouton);

	cancelBouton = new Button(parent.comment.getString("ButtonCancel"));
	cancelBouton.setFont(parent.police);
	cancelBouton.addActionListener(this);
	cancelBouton.setActionCommand("Cancel");
	c.insets = new Insets(3,6,6,3);
	c.gridx = 0;
	c.gridy = 6;
	c.gridwidth = 1;
	c.gridheight = 1;
	gridbag.setConstraints(cancelBouton,c);
        add(cancelBouton);

        /**
	closeBouton = new Button(parent.comment.getString("ButtonClose"));
	closeBouton.setFont(parent.police);
	closeBouton.addActionListener(this);
	closeBouton.setActionCommand("Close");
	c.insets = new Insets(3,3,6,6);
        c.gridx = 2;
        c.gridy = 7;
        gridbag.setConstraints(closeBouton,c);
        add(closeBouton);
	**/


	updateList();

	pack();
    }

    /** Action du bouton Cancel : Annuler l'édition de l'utilisateur
	courant.
    */
    protected void cancelEntry() {
	nomTexte.setText("");
	updateList();
    }
    
    /** Action du bouton Edit : Activer les champs nomTexte et
	choixCouleur pour permettre de modifier le nom et la couleur
	de chaque utilisateur.
	Focus automatique sur le champ nomTexte.
    */
    protected void modifyEntry(User u) {
	nomTexte.setText(u.getUserName());
	//choixCouleur.select(u.getUserColor());
	//engagedChbx.setState(u.getUserEngaged());
	listUsers.setEnabled(false);
	editBouton.setEnabled(false);
	nouveauBouton.setEnabled(false);
	deleteBouton.setEnabled(false);
	nomTexte.setEnabled(true);
	nomTexte.requestFocus();
	saveCloseBouton.setActionCommand("Save");
	saveCloseBouton.setLabel(parent.comment.getString("ButtonSave"));
	cancelBouton.setEnabled(true);
    }

    /** Action du bouton New : créer un nouvel utilisateur (une
	instance par défaut) et modifier ses attributs par "Edit".
    */
    protected void newEntry() {
	cu = new User();
	modifyEntry(cu);
    }

    /** Action du bouton Save : enregistrer les nouveaux attributs de
	l'utilisateur concerné. Puis on met à jour la liste des utilisateurs.
    */
    protected void saveEntry() {
	cu.setUserName(nomTexte.getText());
	parent.addUser(nomTexte.getText(), true); 

	updateList();
   }

    /** Action du bouton Delete : supprimer l'utilisateur
	sélectionné. ATTENTION : il sera impossible (en l'etat actuel 
	des choses) de revenir sur cette action. Les objets crées
	par cet utilisateur ne pourront plus être dessinés, car on a
	besoin pour cela de la couleur de l'auteur...
    */
    protected void deleteEntry() {
	Vector v = parent.argu.getProtagonists();
	cu = (User) v.elementAt(listUsers.getSelectedIndex());
	parent.removeUser(cu.getUserName());
	updateList();
    }
    
    /** Gestion des actions faites sur les boutons.
     */
    public void actionPerformed(ActionEvent event) {
        if (event.getActionCommand() == "Save") {
	    saveEntry();
        }
        else if (event.getActionCommand() == "Cancel") {
	    cancelEntry();
        }
        else if (event.getActionCommand() == "Close") {
	    setVisible(false);
        }
        else if (event.getActionCommand() == "New") {
	    newEntry();
        }
        else if (event.getActionCommand() == "Edit") {
	    Vector v = parent.argu.getProtagonists();
	    cu = (User) v.elementAt(listUsers.getSelectedIndex());
	    modifyEntry(cu);
        }
        else if (event.getActionCommand() == "Delete") {
	    deleteEntry();
        }
    }
   
 
    /** Mise à jour de l'affichage de la liste des utilisateurs.
	On procède par destruction et reconstruction de l'objet
	graphique (List) à partir de la variable stockant la liste des 
	utilisateurs (l'attribut protagonists de la classe
	Argumentation).
	@see Argumentation#getProtagonists
     */
    protected void updateList() {
	User u;
	
	Vector users = parent.argu.getProtagonists();
	
	if (listUsers.getItemCount() > 0) listUsers.removeAll();
	for (int i = 0 ; i < users.size() ; i++) {
	    u = (User) users.elementAt(i);
	    listUsers.add(u.getUserName());
	}
	
	if (users.isEmpty()) {
	    cu = null;
	    editBouton.setEnabled(false);
	    deleteBouton.setEnabled(false);
	    listUsers.setEnabled(false);
	}
	else {
	    cu = (User) users.firstElement();
	    listUsers.select(0);
	    editBouton.setEnabled(true);
	    deleteBouton.setEnabled(true);
	    listUsers.setEnabled(true);
	    
	}
	nomTexte.setEnabled(false);
	saveCloseBouton.setLabel(parent.comment.getString("ButtonClose"));
	saveCloseBouton.setActionCommand("Close");
	cancelBouton.setEnabled(false);
	
	nouveauBouton.setEnabled(true);
    }
}

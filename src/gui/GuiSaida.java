package gui;

import contoller.ControleSaida;
import excecoes.ProdutoNaoEncontrado;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import jdbc.Conectar;
import model.Saidas;
import sun.swing.table.DefaultTableCellHeaderRenderer;

public class GuiSaida extends JPanel{
    private JLabel lbProduto, lbQtd, lbDtSaida, lbDtVal;
    private JLabel lbBProduto, lbBQtd, lbBDtSaida, lbBDtVal;
    private JTextField tfProd, tfQtd, tfDtSaida, tfCodEst, tfDtVal;
    private JTextField tfBProd, tfBQtd, tfBDtSaida, tfBCodEst, tfBDtVal;
    private JButton btAdicionar, btRemover, btRegistrar,btLimpar;
    private JButton btBAdicionar, btBuscar, btBLimpar;
    private JComboBox cbProdutos;
    private JComboBox cbBProdutos;
    
    private JPanel pnPrincipal, pnTabelaCad, pnTabelaBusca;
    private JScrollPane spTabelaCad, spTabelaBusca;
    private JTable tbTabelaCad, tbTabelaBusca;
    private ControleSaida contSaida;
    private SimpleDateFormat sdf;
    private Conectar conectar;
    private Connection connection;
    private PreparedStatement st;
    private ResultSet result;
    private String sql, resultado;
  
    private JPanel pn1, pn2;
    private JTabbedPane tpAba;
   
    
    public GuiSaida(){
        contSaida = new ControleSaida();
        inicializarComponentes();
        definirEventos();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        
  
    }
    
    public void inicializarComponentes(){
        setLayout(null);
        String[] produtos = listarProdutos();
        String[] prodEst = buscaItensSaida();
        //String[] saidaEst = buscarSaidasProdutos();
        
        
        lbProduto = new JLabel("Produtos");
        lbQtd = new JLabel("Qtd");
        lbDtSaida = new JLabel("Data Saída");
        lbDtVal = new JLabel("Data de Val.");
                
        
        cbProdutos = new JComboBox(produtos);
        
        //tfProd = new JTextField();
        tfQtd = new JTextField();
        tfDtSaida = new JTextField();
        tfDtVal = new JTextField();
          
        
        btAdicionar = new JButton("Adicionar");
        btRemover = new JButton("Remover");
        btRegistrar = new JButton("Registrar");
        btBuscar = new JButton("Buscar");
        btLimpar = new JButton("Limpar");
        btBLimpar = new JButton("Limpar");
        
        
                             //X   Y COMP  ALT
        lbProduto.setBounds(25, 30, 60, 25);
        cbProdutos.setBounds(95, 30, 180, 25);
        //tfProd.setBounds(90, 30, 200, 25);
        
        lbDtVal.setBounds(290, 30, 90, 25);
        tfDtVal.setBounds(390, 30, 70, 25);
        
        
        lbQtd.setBounds(485, 30, 30, 25);
        tfQtd.setBounds(525, 30, 45, 25);
        
        btAdicionar.setBounds(100,80,100,25);
        btRemover.setBounds(210,80,90,25);
        //btBAdicionar.setBounds(100,80,100,25);
        
        
        btRegistrar.setBounds(100, 345, 100, 25);
        
        btLimpar.setBounds(210,345,80,25);
        
        //Abas
        
        pn1 = new JPanel(getLayout()); 
        
       
            
        //pn1 add
        
        pnPrincipal = new JPanel();
        pnPrincipal.setLayout(null);
        pnPrincipal.setBounds(0, 0, 600, 400);
        
        pn1.add(lbProduto);
        pn1.add(cbProdutos);
        
        pn1.add(lbQtd);
        pn1.add(tfQtd);
        pn1.add(lbDtSaida);
        pn1.add(tfDtSaida);
        pn1.add(btAdicionar);
        pn1.add(btRemover);
        pn1.add(btRegistrar);
        pn1.add(btLimpar);
        pn1.add(lbDtVal);
        pn1.add(tfDtVal);
        
        // ABA BUSCAR
        
        lbBProduto = new JLabel("Produtos");
        lbBQtd = new JLabel("Qtd");
        lbBDtSaida = new JLabel("Data Saída");
        lbBDtVal = new JLabel("Data de Val.");
                
        
        cbBProdutos = new JComboBox(prodEst);
        
        //tfBProd = new JTextField();
        tfBQtd = new JTextField();
        tfBDtSaida = new JTextField();
        tfBDtVal = new JTextField();
        
                             //X   Y COMP  ALT
        lbBProduto.setBounds(25, 30, 60, 25);
        cbBProdutos.setBounds(95, 30, 180, 25);
        //tfBProd.setBounds(90, 30, 200, 25);
        
        lbBDtSaida.setBounds(25, 30, 90, 25);
        tfBDtSaida.setBounds(105, 30, 70, 25);
        
        
        lbBQtd.setBounds(485, 30, 30, 25);
        tfBQtd.setBounds(525, 30, 45, 25);
        
        //btBAdicionar = new JButton("Adicionar");
        btBuscar.setBounds(100,80,100,25);
        btBLimpar.setBounds(210,80,90,25);
        
        //pn2 add
        pn2 = new JPanel(getLayout());
        
//        pn2.add(lbBProduto);
//        pn2.add(cbBProdutos);
        
        
//        pn2.add(lbBQtd);
//        pn2.add(tfBQtd);
        pn2.add(lbBDtSaida);
        pn2.add(tfBDtSaida);
        
        //pn2.add(btBAdicionar);
        pn2.add(btBuscar);
        pn2.add(btBLimpar);
        
        //Abas
            
        tpAba = new JTabbedPane();
          
        tpAba.setBounds(0, 0, 600, 500);
          
        tpAba.add("Cadastro", pn1);
        tpAba.add("Buscar", pn2);
        
        pnTabelaCad = new JPanel(new BorderLayout());
        pnTabelaCad.setBorder(new TitledBorder("Itens de Saída"));
        spTabelaCad = new JScrollPane();
        DefaultTableModel tableModel = new DefaultTableModel(
            new String[]{"PRODUTOS","QTD","DATA SAÍDA", "DATA VALIDADE"},0){
                public boolean iscellEditable(int row, int col){
                    if(col == 3){
                        return false;
                    }
                    return true;
                }
            };
        
        tbTabelaCad = new JTable(tableModel);
        
        DefaultTableCellHeaderRenderer alinharDireita = new DefaultTableCellHeaderRenderer();
        alinharDireita.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tbTabelaCad.getColumnModel().getColumn(0).setPreferredWidth(275);
        tbTabelaCad.getColumnModel().getColumn(0).setResizable(false);
        tbTabelaCad.getColumnModel().getColumn(1).setResizable(false);
        tbTabelaCad.getColumnModel().getColumn(1).setPreferredWidth(40);
        tbTabelaCad.getColumnModel().getColumn(2).setResizable(false);
        tbTabelaCad.getColumnModel().getColumn(2).setPreferredWidth(110);
        tbTabelaCad.getColumnModel().getColumn(2).setCellRenderer(alinharDireita);
        tbTabelaCad.getColumnModel().getColumn(3).setResizable(false);
        tbTabelaCad.getColumnModel().getColumn(3).setPreferredWidth(110);
        tbTabelaCad.getColumnModel().getColumn(3).setCellRenderer(alinharDireita);
      


        tbTabelaCad.getTableHeader().setReorderingAllowed(false);
        tbTabelaCad.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        spTabelaCad.setViewportView(tbTabelaCad);
        pnTabelaCad.add(spTabelaCad);
        pnTabelaCad.setBounds(20, 125, 550, 200);
        
        //Tabela de Busca
        
        pnTabelaBusca = new JPanel(new BorderLayout());
        pnTabelaBusca.setBorder(new TitledBorder("Itens de Busca"));
        spTabelaBusca = new JScrollPane();
        DefaultTableModel tableModelBusca = new DefaultTableModel(
            new String[]{"PRODUTOS","QTD"},0){
                public boolean iscellEditable(int row, int col){
                    if(col == 3){
                        return false;
                    }
                    return true;
                }
            };
        
        tbTabelaBusca = new JTable(tableModelBusca);
        
        //DefaultTableCellHeaderRenderer alinharDireitaBusca = new DefaultTableCellHeaderRenderer();
        alinharDireita.setHorizontalAlignment(SwingConstants.RIGHT);
        
        tbTabelaBusca.getColumnModel().getColumn(0).setPreferredWidth(325);
        tbTabelaBusca.getColumnModel().getColumn(0).setResizable(false);
        tbTabelaBusca.getColumnModel().getColumn(1).setResizable(false);
        tbTabelaBusca.getColumnModel().getColumn(1).setPreferredWidth(50);


       
      


        tbTabelaBusca.getTableHeader().setReorderingAllowed(false);
        tbTabelaBusca.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        spTabelaBusca.setViewportView(tbTabelaBusca);
        pnTabelaBusca.add(spTabelaBusca);
        pnTabelaBusca.setBounds(35, 125, 390, 200);
        
        
        
        pn1.add(pnTabelaCad);
        pn2.add(pnTabelaBusca);
        
        pnPrincipal.add(tpAba);
        
        add(pnPrincipal);
    }
    
    public void definirEventos(){
        cbProdutos.requestFocus();
        
        btAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                
                
                if(cbProdutos.getSelectedItem().equals("") || tfQtd.getText().equals("") || tfDtVal.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "Preencha todos os campos");
                    return;
                }
                DefaultTableModel dtm = (DefaultTableModel)tbTabelaCad.getModel();
                dtm.addRow(new Object[]{
                   
                    cbProdutos.getSelectedItem(),
                    tfQtd.getText(),
                    sdf.format(getDataAtual()),
                    tfDtVal.getText()

               });
              
               
               return ;
            }
        });

        btRemover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                int[] linhas = tbTabelaCad.getSelectedRows();
                DefaultTableModel dtm = (DefaultTableModel) tbTabelaCad.getModel();
                for(int i = (linhas.length-1);i>=0;i--){
                    dtm.removeRow(linhas[i]);
                }
            }
        });
        btLimpar.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    
                    limpar();
                    
                }
            });
        
        btRegistrar.addActionListener(new ActionListener() {
            @Override
            
            public void actionPerformed(ActionEvent ae) {
                ArrayList<Saidas> saidas = new ArrayList<Saidas>();
                
                for (int linha = 0; linha <tbTabelaCad.getRowCount(); linha++){
                    Saidas saida = new Saidas();
                    for(int coluna = 0; coluna< tbTabelaCad.getColumnCount(); coluna++){  
                       
                        switch(coluna){
                            case 0:
                                String nome = ""+tbTabelaCad.getValueAt(linha, coluna);
                                saida.setNomePro(nome);
                                break;
                            case 1: 
                                
                                int qtd = Integer.parseInt(""+tbTabelaCad.getValueAt(linha, coluna));
                                saida.setQuantidadeSai(qtd);
                                break;
                            
                            case 2:
                              
                                saida.setDataSaida(getDataAtual());
                                
                            case 3:    
                                String data = tfDtVal.getText();
                                Date dataVal = null;
                                
                                try {
                                    dataVal = sdf.parse(data);
                                } catch (ParseException ex) {
                                    Logger.getLogger(GuiSaida.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                
                                saida.setDataVal(dataVal);
                                break;  
                            default:
                                JOptionPane.showMessageDialog(null, "Não há produtos na lista.");
                        }
                        
                    }
                    saidas.add(saida);
                    
                } 
                try {
                    
                    contSaida.cadastrar(saidas);
                } catch (SQLException ex) {
                    Logger.getLogger(GuiSaida.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ProdutoNaoEncontrado ex) {
                    Logger.getLogger(GuiSaida.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                limpar();
            }

        });
        
        //Preciso continuar daqui, inserindo o nome do produto ou a data de saida
        
        btBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                ArrayList<Saidas> saidas = new ArrayList<Saidas>();
                //String nomeBuscaPro = (String) cbBProdutos.getSelectedItem();
                if(tfBDtSaida.getText().equals("") ){
                    JOptionPane.showMessageDialog(null, "Preencha a Data de Saída");
                    return;
                }
                
                String dataSaida = tfBDtSaida.getText();
               
                Date data = null;
                
                try {
                    data = sdf.parse(dataSaida);
                } catch (ParseException ex) {
                    Logger.getLogger(GuiSaida.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                saidas = contSaida.buscarSaidasProdutos(data);
                
                for(Saidas saida : saidas){
                    
                    DefaultTableModel dtm = (DefaultTableModel)tbTabelaBusca.getModel();
                    dtm.addRow(new Object[]{
                        saida.getNomePro(),
                        saida.getQuantidadeSai(),
                        dataSaida
                    });
                }
            }   
        });
        
        
        btBLimpar.addActionListener(new ActionListener() {
            @Override
                public void actionPerformed(ActionEvent e) {
                    
                    limpar();
                }
            });
        
    }
    private void limpar(){
        cbBProdutos.setSelectedItem("");
        cbProdutos.setSelectedItem("");
        tfBDtVal.setText("");
        tfDtVal.setText("");
        tfQtd.setText("");
        tfBQtd.setText("");
        tfDtSaida.setText("");
        tfBDtSaida.setText("");
        
        
        
        
        int linhas = tbTabelaCad.getRowCount();
        //JOptionPane.showMessageDialog(null, "Limpar Tabelas");
        DefaultTableModel dtm = (DefaultTableModel) tbTabelaCad.getModel();
        for(int i = 0; linhas>0; linhas--){
            dtm.removeRow(i);
                }
        int linhasB = tbTabelaBusca.getRowCount();
        
        DefaultTableModel dtmB = (DefaultTableModel) tbTabelaBusca.getModel();
        for(int i = 0; linhasB>0; linhasB--){
            dtmB.removeRow(i);
                }
    }
   
    public static Date getDataAtual(){   
        Calendar c = Calendar.getInstance();    
        return c.getTime(); 
    }
    
    private String[] listarProdutos(){
        String[] prod = contSaida.listarProdutos();
        return prod;
    }
   private String[] buscaItensSaida(){//Não está sendo utilizado
        String[] prodItSai = contSaida.buscaProdEstoque();
        return prodItSai;
    }
      
}

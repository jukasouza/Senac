package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import model.Categorias;
import model.Entradas;
import model.Produtos;

public class CRUDEntrada {
    private Conectar conectar;
    private Connection connection;
    private PreparedStatement st;
    private ResultSet result;
    private String sql, resultado;
    private ArrayList<String> categorias;
    private int contador;

   
    public CRUDEntrada(){
        categorias = new ArrayList<String>();
        conectar = new Conectar();
        connection = conectar.getConnection();
    }
    
    public void cadastrarProduto(Produtos produto, Date dtEnt, Entradas entrada){
            Categorias cat = produto.getCategoria();
            
            int cod = buscarCodPro(produto.getNome());
            int codCat = buscarCodigoCat(cat.getCategoria());
            String descricao = produto.getDescricao();
            String nome = produto.getNome();    
    
            if(cod == 0){
                try{
                    sql = "INSERT INTO tb_produtos(pro_nome, pro_cat_cod, pro_desc) VALUES (?,?,?)";
                    
                    st = connection.prepareStatement(sql);
                    
                    st.setString(1, nome);
                    
                    st.setInt(2, codCat);
                   
                    st.setString(3, descricao);
             
                    st.executeUpdate();
                
                }catch(SQLException e){
                    JOptionPane.showMessageDialog(null, "Erro no cadastro!");
                }
            }else{
                     
            } 
            cadastrarDataEnt(dtEnt, entrada);
    }   
   
    
    public void cadastrar(ArrayList<Entradas> entradas, Date dtEnt){
        contador = 0;
        
        for (Entradas entrada : entradas){
            cadastrarProduto(entrada.getProduto(), dtEnt, entrada);
        }
    }    
    
    public void cadastrarDataEnt(Date dtEnt, Entradas entrada){
        java.util.Date data = dtEnt;
        java.sql.Date dataSql = new java.sql.Date(data.getTime());
        
        if (contador == 0){
            try{
                sql = "Insert Into tb_entradas (ent_dt) values(?)";
                st = connection.prepareStatement(sql);
                st.setDate(1, dataSql);
                st.executeUpdate();
            }catch(SQLException se){
                resultado = "Erro!";
            }
            contador = contador+1;
            //int codEntH = codEnt();
            JOptionPane.showMessageDialog(null,"Este é o Código da sua entrada" + "\n" + codEnt() );
        }else{
            
        }
        cadastrarItensentrada(entrada);
    }
    
    public void cadastrarEstoque(Entradas entrada){
       Produtos produto = new Produtos();

            
            produto = entrada.getProduto();
            int qtdEnt = entrada.getQuantidade();
            int codPro = buscarCodPro(produto.getNome());
            
            java.util.Date data = entrada.getDataVal();
            java.sql.Date dataSqlVal = new java.sql.Date(data.getTime());
            
            try{
                sql = "Insert Into tb_estoques(est_qtd, est_dt_val, est_arm, est_pro_cod) values(?,?,?,?)";
                st = connection.prepareStatement(sql);
                st.setInt(1, qtdEnt);
                st.setDate(2, dataSqlVal);
                st.setString(3, "Geral");
                st.setInt(4,codPro);
                st.executeUpdate();
            }catch(SQLException se){
                resultado = "Erro!";  
        }
    }
        
    public void cadastrarItensentrada(Entradas entrada){
            java.util.Date data = entrada.getDataVal();
            java.sql.Date dataSql = new java.sql.Date(data.getTime());
            
            Produtos prod = entrada.getProduto();
            
            int codPro = buscarCodPro(prod.getNome());
            int codEntr = codEnt();
            int quantidade = entrada.getQuantidade();

            try{
                    sql = "Insert Into tb_it_ent (it_ent_pro_cod, it_ent_ent_cod, it_ent_qtd, it_ent_dt_val) values(?,?,?,?)";
                    st = connection.prepareStatement(sql);
                    st.setInt(1, codPro);
                    st.setInt(2, codEntr);
                    st.setInt(3, quantidade);
                    st.setDate(4, dataSql);
                    st.executeUpdate();
                }catch(SQLException e){
                    resultado = "Erro";
                }
            
        cadastrarEstoque(entrada);
        
        
    }
    
    
    public int buscarCodPro(String nome) {
        int cod = 0;
        try{
            sql = "Select pro_cod From tb_produtos where pro_nome = ?";
            st = connection.prepareStatement(sql);
            st.setString(1, nome);
            result = st.executeQuery();
            while (result.next()){
                cod = result.getInt(1);
            }   
            return cod; 
        } catch(SQLException e){
                resultado = "Erro" ;
                return 0;    
        }
        
    }
    
    public int codEnt(){
        int cod = 0;
        try{
            //sql = "select top 1 ent_cod from tb_entradas order by ent_cod desc";
            sql = "select ent_cod from tb_entradas order by ent_cod desc LIMIT 1;";
            st = connection.prepareStatement(sql);
            result = st.executeQuery();
            while(result.next()){
                cod = result.getInt(1);
            }
            return cod;
        }catch(SQLException e ){
                resultado = "Erro";
                return 0;
        }
        
    }
    
    public ArrayList<String> categorias(){
        categorias.clear();
        try{
            sql = "SELECT * From tb_categorias";
            st = connection.prepareStatement(sql);
            result = st.executeQuery();
            while(result.next()){
                categorias.add(result.getString(2));
            }
            return categorias;
        }catch(SQLException e){
            resultado = "Erro";
            return null;
        }
    }
    
    public int buscarCodigoCat (String categoria){
        int codCat = 0;
        try{
            sql = "Select cat_cod from tb_categorias  where cat_nome = ?";
            st = connection.prepareStatement(sql);
            st.setString(1, categoria);
            result = st.executeQuery();
            while(result.next()){
                codCat = result.getInt(1);
            } 
        return codCat;
        }catch(SQLException e){
            resultado = "Erro";
            return 0;
        }
    }
      
        public ArrayList<String> buscarCodigos(Date data){
            ArrayList<String> codigos = new ArrayList<String>();
            
            java.util.Date data1 = data;
            java.sql.Date dataSql = new java.sql.Date(data1.getTime());
            
            try{
                sql = "SELECT tb_entradas.ent_cod from tb_entradas where tb_entradas.ent_dt = ? ";
                st = connection.prepareStatement(sql);
                st.setDate(1, dataSql);
                result = st.executeQuery();
                while(result.next()){
                    String cod = "";
                    cod = ""+result.getInt(1);
                    codigos.add(cod);
                }
                return codigos; 
            }catch(SQLException se){
                resultado = "Erro!"; 
                return null;
            } 
        }

        
        public ArrayList<Entradas> buscarProdutosCod(int cod){
            
            ArrayList<Entradas> entradas = new ArrayList<Entradas>();

            try{
                sql = "SELECT tb_produtos.pro_nome as 'Produto',\n" +
                "tb_it_ent.it_ent_qtd as 'Quantidade' \n" +
                "from tb_produtos\n" +
                "inner join tb_it_ent\n" +
                "on tb_it_ent.it_ent_pro_cod = tb_produtos.pro_cod\n" +
                "inner join tb_entradas\n" +
                "on tb_entradas.ent_cod = tb_it_ent.it_ent_ent_cod\n" +
                "where tb_entradas.ent_cod = ?";
                st = connection.prepareStatement(sql);
                st.setInt(1, cod);
                result = st.executeQuery();
               
                while(result.next()){
                   Produtos produto = new Produtos();
                    Entradas entrada = new Entradas();
                    
                    produto.setNome(result.getString(1));
                    entrada.setProduto(produto);
                    entrada.setQuantidade(result.getInt(2));
                    entradas.add(entrada);
                }
                return entradas;
            }catch(SQLException se){
                resultado = "Erro!";
                return null;
        }
      }
        public ArrayList<Entradas> buscarProdutosEdi(int cod){
            
            ArrayList<Entradas> entradas = new ArrayList<Entradas>();

            try{
                sql = "SELECT *        from tb_produtos\n" +
"                inner join tb_it_ent \n" +
"                on tb_it_ent.it_ent_pro_cod = tb_produtos.pro_cod\n" +
"                inner join tb_entradas\n" +
"                on tb_entradas.ent_cod = tb_it_ent.it_ent_ent_cod\n" +
"                inner join tb_categorias\n" +
"				on tb_categorias.cat_cod = tb_produtos.pro_cat_cod\n" +
"				where tb_entradas.ent_cod = ?;";
                st = connection.prepareStatement(sql);
                st.setInt(1, cod);
                result = st.executeQuery();
               
                while(result.next()){
                    Produtos produto = new Produtos();
                    Entradas entrada = new Entradas();
                    Categorias categoria = new Categorias();
                    
                    categoria.setCategoria(result.getString(12));
                    produto.setNome(result.getString(2));
                    produto.setDescricao(result.getString(3));
                    produto.setCategoria(categoria);
                    entrada.setProduto(produto);
                    entrada.setDataVal(result.getDate(6));
                    entrada.setQuantidade(result.getInt(5));
                    entradas.add(entrada);
                }
                return entradas;
            }catch(SQLException se){
                resultado = "Erro!";
                return null;
        }
      }
        public ArrayList<String> entDoDia (Date data){
            
            java.util.Date data1 = data;
            java.sql.Date dataDia = new java.sql.Date(data1.getTime());
            
            ArrayList<String> entradas = new ArrayList<String>();
 
            entradas.clear();
    
            try{
                sql = "Select ent_cod from tb_entradas  where ent_dt = ?";
                st = connection.prepareStatement(sql);
                st.setDate(1, dataDia);
                result = st.executeQuery();
                while(result.next()){
                    entradas.add("" + result.getInt(1));
                } 
             
            return entradas;
            }catch(SQLException e){
                resultado = "Erro";
                return null;
            }
    }
      
//    public void editar(){}
//    public void excluir(){}
}

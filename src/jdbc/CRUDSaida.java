package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.JOptionPane;
import model.Saidas;

public class CRUDSaida {
    private Conectar conectar;
    private Connection connection;
    private PreparedStatement st;//Padrão
    private ResultSet result;//Padrão
    private String sql, sql2,sql3, sql4, resultado;
    private ArrayList<String> produtos;
    private ArrayList<Saidas> saidas;
    private ArrayList<String> pedidos;
    private ArrayList<String> categorias;
    private ArrayList<String> estoques;
   
     
    public CRUDSaida(){
        conectar = new Conectar();
        connection = conectar.getConnection();
        saidas = new ArrayList<Saidas>();
        pedidos = new ArrayList<String>();
        produtos = new ArrayList<String>();
        categorias = new ArrayList<String>();
        estoques = new ArrayList<String>();
  
     }
    
    public void cadastrar(ArrayList<Saidas> saidas){
        int contador = 0;
        for (Saidas saida : saidas){
            int codProd = codPRo(saida.getNomePro());

            java.util.Date data2 = saida.getDataVal();
            java.util.Date data3 = saida.getDataSaida();
            java.sql.Date dataValSQL = new java.sql.Date(data2.getTime());

           
            int codEst = 0;
            int qtdEst = 0;

            try{
  
            sql3 = "select 	tb_produtos.pro_cod,\n" +
                        "	tb_estoques.est_cod,\n" +
                        "    	tb_estoques.est_qtd\n" +
                        "	from \n" +
                        "	   	tb_produtos\n" +
                        "	inner join\n" +
                        "	   	tb_estoques\n" +
                        "	on\n" +
                        "	   	tb_produtos.pro_cod = tb_estoques.est_pro_cod\n" +
                        "	where\n" +
                        "	 	tb_produtos.pro_cod = ? and tb_estoques.est_dt_val = ? and \n" +
                        "               tb_estoques.est_arm = 'Nutrição';";

            st = connection.prepareStatement(sql3);
            st.setInt(1, codProd);//Estou buscando no sql
            st.setDate(2, dataValSQL);//Estou buscando no sql
            result = st.executeQuery();
            while(result.next()){
                codEst = result.getInt(2);//Estou pegando o campo 2 do sql
                qtdEst = result.getInt(3);//Estou pegando o campo 3 do sql
                
            }
            //JOptionPane.showMessageDialog(null,"Cod Est: "+codEst);
            //JOptionPane.showMessageDialog(null,"Qtd Est: "+qtdEst);
            }catch(SQLException e){
                JOptionPane.showMessageDialog(null, "Erro no Armazem Saída!");
            }
            
            int resultado1 = 0;
               if(saida.getQuantidadeSai() > qtdEst || codEst == 0){

                    JOptionPane.showMessageDialog(null, "Quantidade insuficiente ou produto com Data Validade Errada!!");
               }else{
                    resultado1 = qtdEst - saida.getQuantidadeSai();//Realizando a baixa no estoque
                    moviEsto(codEst, resultado1);
                    java.util.Date data1 = saida.getDataSaida();
                    java.sql.Date dataSaidaSQL = new java.sql.Date(data1.getTime());


                    if(contador == 0){

                        try{
                        sql = "insert into tb_saidas(sai_dt) VALUES (?);";
                        st = connection.prepareStatement(sql);
                        st.setDate(1, dataSaidaSQL);
                        st.executeUpdate();
                        }catch(SQLException e){
                            JOptionPane.showMessageDialog(null, "Erro na Data Saída!");

                        }
                        contador = contador + 1;
                    }
                    int codSaid = codSai();


                    try{

                    sql2 = "insert into tb_it_sai(it_sai_qtd, it_sai_pro_cod, it_sai_sai_cod) VALUES (?,?,?);";
                    st = connection.prepareStatement(sql2);
                    st.setInt(1,saida.getQuantidadeSai());
                    st.setInt(2, codProd );
                    st.setInt(3, codSaid);
                    st.executeUpdate();
                    }catch(SQLException e){
                        JOptionPane.showMessageDialog(null, "Erro nos Itens Saída!");
                    }

                    if(resultado1 == 0){
                        try{
                            sql4 = "DELETE FROM tb_estoques WHERE tb_estoques.est_cod = ?";
                            st = connection.prepareStatement(sql4);
                            st.setInt(1, codEst);
                            st.executeUpdate();
                        }catch(SQLException e){
                            JOptionPane.showMessageDialog(null, "Erro ao Deletar o estoque!");
                        }
                    }
                }
        }        
    }
    
    
    public ArrayList<String> listarProdutos(){
            
            produtos.clear();    
            String produto = "";

            
        try{ 
            sql = "SELECT * FROM tb_produtos\n" +
"                    INNER JOIN tb_estoques\n" +
"                    ON tb_produtos.pro_cod = tb_estoques.est_pro_cod\n" +
"                    WHERE tb_estoques.est_arm = 'Nutrição'" +
"                    GROUP BY tb_produtos.pro_nome\n" +
"                    ORDER BY tb_estoques.est_dt_val ASC;";
            
            st = connection.prepareStatement(sql);
            //st.setInt(1, 2);

            
            result = st.executeQuery();
            while(result.next()){
                produtos.add(result.getString(2));//Refere-se à segunda coluna buscada pelo sql 
                

            }
            return produtos;
        }catch(SQLException e){
            resultado = "Erro!";
            return null;
        }
    }
    
    public int codSai(){
        int cod = 0;
        try{
            //sql = "select top 1 ent_cod from tb_entradas order by ent_cod desc";
            sql = "select sai_cod from tb_saidas order by sai_cod desc LIMIT 1";
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
    public int codPRo(String nome) {
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
    
    private void moviEsto(int codEst, int resultadoEst){
        
        try{
            sql ="UPDATE tb_estoques SET est_qtd = ? WHERE tb_estoques.est_cod = ?;";
            st = connection.prepareStatement(sql);
            st.setInt(1, resultadoEst);
            st.setInt(2, codEst);
            st.executeUpdate();
        }catch(SQLException e){
            resultado = "Erro!";
            JOptionPane.showMessageDialog(null, "ERRO");
        }
    }
    
    public ArrayList<String> buscaProdEstoques(){ //Está na aba de buscar, mas será corrigida
        estoques.clear();
        try{
            sql = "SELECT  tb_produtos.pro_nome\n" +
                    "FROM `tb_produtos` \n" +
                    "GROUP BY tb_produtos.pro_nome\n" +
                    "ORDER BY tb_produtos.pro_nome ASC";
            st = connection.prepareStatement(sql);
            result = st.executeQuery();
            while(result.next()){
                estoques.add(result.getString(1));
                //estoques.add(result.getString(2));
                //estoques.add(result.getString(3));
            }
            return estoques;
        }catch(SQLException e){
            resultado = "Erro na BuscaEstoque";
            return null;
        }
    }
    
    public ArrayList<Saidas> buscarSaidasProdutos(Date data){
        //Busca por data
        
        ArrayList<Saidas> saidas = new ArrayList<Saidas>();
        
//        java.util.Date dataBusc = data;
        java.sql.Date dataSaidaSQL = new java.sql.Date(data.getTime());
        try{
            sql = "SELECT tb_produtos.pro_nome as 'Produto',\n" +
                    "sum(tb_it_sai.it_sai_qtd) as 'Quantidade',\n" +
                    "tb_saidas.sai_dt AS 'Data Saída',\n" +
                    "tb_saidas.sai_cod as 'Cód. Saída'\n" +
                    "from tb_produtos\n" +
                    "inner join tb_it_sai\n" +
                    "on tb_it_sai.it_sai_pro_cod = tb_produtos.pro_cod\n" +
                    "inner join tb_saidas\n" +
                    "on tb_saidas.sai_cod = tb_it_sai.it_sai_sai_cod\n" +
                    "where \n" +
                    "tb_saidas.sai_dt = ?\n" +
                    "GROUP BY tb_produtos.pro_nome ASC";
            st = connection.prepareStatement(sql);         
            st.setDate(1, dataSaidaSQL);
            result = st.executeQuery();
            
            
            while(result.next()){
                
                Saidas saida = new Saidas();
                saida.setNomePro(result.getString(1));
                saida.setQuantidadeSai(result.getInt(2));
                saida.setDataSaida(dataSaidaSQL);
                
                saidas.add(saida);    
            }
            
            
        }catch(SQLException e){
            resultado = "Erro na Busca de Saídas Estoque";
            return null;
        }
        
        
        return saidas;
    }
    
    
    
       public void excluir(String nome){}
    
    

    
    // Maia
    
    
    
    
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
       
}

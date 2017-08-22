package contoller;

import java.util.ArrayList;
import java.util.Date;
import jdbc.CRUDEntrada;
import model.Entradas;

public class ControleEntrada {
    private CRUDEntrada crudEntrada;
    
    public ControleEntrada() {
        crudEntrada = new CRUDEntrada();
    }
    
    
    
    public void cadastrar(ArrayList<Entradas> entradas, Date data){   
        crudEntrada.cadastrar(entradas, data);
    }
    
    public String[] listarCategorias(){
        ArrayList<String> categorias = crudEntrada.categorias();
        String[] cat = new String[categorias.size()+1];
        int i=1;
        cat[0] = "";
        for(String cate : categorias){
            cat[i++]= cate;
        }
        return cat;
    }
    
    public ArrayList<String> buscarCodigos(Date data){
        ArrayList<String> codigos = new ArrayList<String>();
        
        codigos =  crudEntrada.buscarCodigos(data);
   
        return codigos;
      
    }
    
    public ArrayList<Entradas> buscarProdutos(int cod){
        ArrayList<Entradas> entradas = new ArrayList<Entradas>();
        entradas = crudEntrada.buscarProdutosCod(cod);
        return entradas;
    }
    
    public ArrayList<Entradas> buscarProdutosEdi(int cod){
        ArrayList<Entradas> entradas = new ArrayList<Entradas>();
        entradas = crudEntrada.buscarProdutosEdi(cod);
        return entradas;
    }
    
    public String[] entDoDia(Date data){
        ArrayList<String> entradas = new ArrayList<String>();
        entradas = crudEntrada.entDoDia(data);
        
        String[] cat = new String[entradas.size()];
        int i=0;
        for(String cate : entradas){
            cat[i++]= cate;
        }
        
        return cat;
    } 
}

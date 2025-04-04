

import java.util.LinkedList;
import java.util.Queue;
/**
 * Clase que implementa un arbol AVL
 * @param <T> Tipo de dato que almacena el arbol AVL
 * @author Saul Ubaldo Rojas Vazquez
 * @created 14/03/2025
 */
public class ArbolAVL<T extends Comparable<T>> {
    private int cont; // numero de elementos del arbol AVL
    private NodoAVL<T> raiz; // nodo raiz del arbol

    public ArbolAVL() {
        this.cont = 0;
        this.raiz = null;
    }
      /**
     * Indica si el elemento existe en el arbol
     * @return True si está el elemento, false si no
     */
    public boolean busca(T element){
        return busca(element,raiz) != null;
    }

    /**
     * Devuelve el nodo del elemento buscado
     * @return nodo buscado
     */

    private NodoAVL<T> busca(T element,NodoAVL<T> actual){
        if(actual == null)
            return null;
        if(element.compareTo(actual.getElem())==0)
            return actual;
        if(element.compareTo(actual.getElem())>0)
            return busca(element,actual.getDer());
        return busca(element,actual.getIzq());
    }

    
    /**
     * Método que realiza la rotación para los casos izq-der, der-izq
     * @param alpha si es el caso izq-der, beta si es el caso der-izq
     * @param beta si es el caso izq-der, alpha si es el caso der-izq
     * @param gamma es el nodo mas bajo en la rotacion
     */
    public void rotacionComb(NodoAVL<T> alpha, NodoAVL<T> beta, NodoAVL<T> gamma){
        NodoAVL<T> B,C;
        B = gamma.getIzq();
        C = gamma.getDer();
        beta.cuelga(B,true);
        alpha.cuelga(C,false);
        gamma.cuelga(beta,false);
        gamma.cuelga(alpha,true);
        //Se vuelven a conectar los nodos

        beta.setFactorEquilibrio(0);
        alpha.setFactorEquilibrio(0);
        //Se actualizan los fe
        if(gamma.getFactorEquilibrio()==1)
            beta.setFactorEquilibrio(-1);
        else if(gamma.getFactorEquilibrio()==-1)
            alpha.setFactorEquilibrio(1);
        gamma.setFactorEquilibrio(0);
    }
    
    /**
     * Método que realiza la rotación
     * @param alpha Nodo a partir del cual se rota
     */
    public NodoAVL<T> rotar(NodoAVL<T> alpha){
        NodoAVL<T> beta,gamma,A,B,C,D;
        if(alpha.getFactorEquilibrio() < 0){ // caso en el que la rama izq es mas larga
            beta = alpha.getIzq();
            if(beta.getFactorEquilibrio() <= 0){ // caso rotacion izq -izq
                gamma = beta.getIzq();
                C = beta.getDer();
                if(alpha == raiz){
                    beta.setPapa(null);
                    raiz = beta;
                }else{
                    alpha.getPapa().cuelga(beta);
                }
                alpha.cuelga(C,false);
                beta.cuelga(alpha, true);
                //actualizacion de factores de equilibrio
                if(beta.getFactorEquilibrio() == -1){
                    alpha.setFactorEquilibrio(0);
                    beta.setFactorEquilibrio(0);
                }else{
                    alpha.setFactorEquilibrio(-1);
                    beta.setFactorEquilibrio(1);
                }
                D = beta;
            }else{ // caso rotacion izq-der
                gamma = beta.getDer();
                if(alpha == raiz){
                    gamma.setPapa(null);
                    raiz = gamma;
                }else{
                    alpha.getPapa().cuelga(gamma);
                }
                rotacionComb(alpha,beta,gamma);
                D = gamma;
            }
        }else{ //caso en el que la rama derecha es mas larga
            beta = alpha.getDer();
            if(beta.getFactorEquilibrio() >= 0){ // caso rotacion der-der
                gamma = beta.getDer();
                B = beta.getIzq();
                if(alpha == raiz){
                    beta.setPapa(null);
                    raiz = beta;
                }else
                    alpha.getPapa().cuelga(beta);
                alpha.cuelga(B,true);
                beta.cuelga(alpha,false);
                beta.cuelga(gamma,true);
                //actualizacion de factores de equilibrio
                if(beta.getFactorEquilibrio() == 1){
                    alpha.setFactorEquilibrio(0);
                    beta.setFactorEquilibrio(0);
                }else{
                    alpha.setFactorEquilibrio(1);
                    beta.setFactorEquilibrio(-1);
                }
                D = gamma;
            }else{ // caso der-izq
                gamma = beta.getIzq();
                if(alpha == raiz){
                    gamma.setPapa(null);
                    raiz = gamma;
                }
                else
                    alpha.getPapa().cuelga(gamma);
                rotacionComb(beta, alpha, gamma);
                D = gamma;
            }
        }
        return D;
    }
     /**
     * Método para insertar en el árbol
     * @param element elemento a insertar
     */
    public void insertar(T element){
        boolean existe = busca(element);
        NodoAVL<T> actual = raiz;
        if(existe)
            return;
        if(raiz == null){
            raiz = new NodoAVL<T>(element);
            actual = raiz;
        }
        else{
            boolean flag = false;
            while(!flag){ // busqueda del lugar en donde debe insertarse el nodo
                if(element.compareTo(actual.getElem()) <= 0){
                    if(actual.getIzq() == null){
                        actual.cuelga(new NodoAVL<T> (element),false);
                        flag = true;
                    }
                    actual = actual.getIzq();
                }else{
                    if(actual.getDer() == null){
                        actual.cuelga(new NodoAVL<T> (element),true);
                        flag = true;
                    }
                    actual = actual.getDer();
                }
            }
        }
        //actualizacion del numero de elementos en el arbol
        cont++;

        NodoAVL<T> papa = actual.getPapa();
        //actualizacion de los fe en el arbol
        while(papa!= null){
            if(actual == papa.getIzq())
                papa.decrementaFe();
            else
                papa.incrementaFe();
            if(papa.getFactorEquilibrio()==-1 || papa.getFactorEquilibrio()==1){
                actual = papa;
                papa = papa.getPapa();
            }else{
                if(papa.getFactorEquilibrio() == -2 || papa.getFactorEquilibrio() == 2){
                    rotar(papa);
                }
                papa = null;
            }
        }
    }
    /**
     * Método para eliminar un elemento del árbol
     * @param element  elemento a eliminar
     * @return regresa el valor del elemento o un null
     */
    public T borrar(T element){
        T res;
        NodoAVL<T> actual = busca(element,raiz);
        if(actual == null){ // caso en el que no encuentra el dato
            res = null;
        }else{ //caso en el que esta el dato en el arbol
            res = actual.getElem();
            if(actual.getDer() == null && actual.getIzq() == null){ // caso en el que es una hoja
                if(actual==raiz) // caso en que la hoja es la raiz
                    raiz = null;
                else{
                    NodoAVL<T> papa = actual.getPapa();
                    if(actual == papa.getIzq())
                        papa.setIzq(null);
                    else
                        papa.setDer(null);
                }
            }else if(actual.getIzq()!= null && actual.getDer()!=null){ // caso en el que tiene dos hijo
                NodoAVL<T> sucesor = actual.getDer();
                while(sucesor.getIzq()!=null){ // sucesor in-orden
                    sucesor=sucesor.getIzq();
                }
                actual.setElem(sucesor.getElem());
                sucesor.getPapa().cuelga(sucesor.getDer(),sucesor == sucesor.getPapa().getDer()); // lo cuelga de lado izuquierdo si no es hijo derecho del padre
                if(sucesor.getPapa() == actual){
                    actual.decrementaFe();
                    if(actual.getFactorEquilibrio()==-1)
                        return res;
                    else if(actual.getFactorEquilibrio()==-2){
                        actual = rotar(actual);
                        if(actual.getFactorEquilibrio()!=0)
                        return res;
                    }
                }
                else
                    actual = sucesor;

            }else{ // caso en el que tiene un solo hijo
                NodoAVL<T> hijo = actual.getIzq();
                if(hijo == null)
                    hijo = actual.getDer();
                if(actual == raiz){ // el arbol solo tiene un hijo desde la raiz
                    raiz = hijo;
                    raiz.setPapa(null);
                }
                else
                    actual.getPapa().cuelga(hijo);
            }

            NodoAVL<T> papa = actual.getPapa();
            //actualiza los factores de equilibrio
            while(papa!=null){
                if(actual.getElem().compareTo(papa.getElem())<=0){
                    papa.incrementaFe();
                }else{
                    papa.decrementaFe();
                }
                if(papa.getFactorEquilibrio()==0){
                    actual = papa;
                    papa = actual.getPapa();
                }
                else if(papa.getFactorEquilibrio()==1||papa.getFactorEquilibrio()==-1){
                    papa = null;
                }else{
                    actual = rotar(papa);
                    if(actual.getFactorEquilibrio()!=0)
                        papa = null;
                    else
                        papa = actual.getPapa();
                }
            }
            cont--;
        }
        return res;
    }
    
    public String imprimerNivel(){
        StringBuilder sb = new StringBuilder();
        Queue<NodoAVL<T>> cola = new LinkedList<>();
        cola.add(raiz);
        int i=0;
        while(!cola.isEmpty()){
            i++;
            NodoAVL<T> nodo = cola.poll();
            if(nodo!=null){
                sb.append(nodo.getElem()+"-"+nodo.getFactorEquilibrio()+" ");
                cola.add(nodo.getIzq());
                cola.add(nodo.getDer());
            }else{
                sb.append("_._ ");
            }
            if((i & (i+1)) == 0){
                sb.append("\n");
            }
        }
        return sb.toString();
    }
}

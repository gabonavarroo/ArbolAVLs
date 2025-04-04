
/**
 * Clase que representa un nodo de un arbol AVL
 * @param <T> Tipo de dato que almacena el nodo
 * @author Saul Ubaldo Rojas Vazquez
 * @created 14/03/2025
 */

public class NodoAVL<T extends Comparable<T>> {
    T elem;
    NodoAVL<T> izq, der, papa;
    private int factorEquilibrio;

      /**
     * Constructor del nodo
     * @param dato: dato T que almacena el nodo
     */

    public NodoAVL(T elem) { 
        this.elem = elem;
        this.izq = null;
        this.der = null;
        this.papa = null;
        this.factorEquilibrio=0;
    }
    public NodoAVL(T elem, NodoAVL<T> izq, NodoAVL<T> der, NodoAVL<T> papa) {
        this.elem = elem;
        this.izq = izq;
        this.der = der;
        this.papa = papa;
    }

    public int getFactorEquilibrio() {
        return factorEquilibrio;
    }
    public void setFactorEquilibrio(int factorEquilibrio) {
        this.factorEquilibrio = factorEquilibrio;
    }

    public T getElem() {
        return elem;
    }

    public void setElem(T elem) {
        this.elem = elem;
    }

    public NodoAVL<T> getIzq() {
        return izq;
    }

    public void setIzq(NodoAVL<T> izq) {
        this.izq = izq;
    }

    public NodoAVL<T> getDer() {
        return der;
    }

    public void setDer(NodoAVL<T> der) {
        this.der = der;
    }

    public NodoAVL<T> getPapa() {
        return papa;
    }

    public void setPapa(NodoAVL<T> papa) {
        this.papa = papa;
    }

    public void incrementaFe(){
        factorEquilibrio++;
    }

    public void decrementaFe(){
        factorEquilibrio--;
    }

      /**
     * Método que cuelga un nodo debajo
     * @param hijo Nodo que se quiere colgar
     * @param lado designa el lado del que se quiere colgar el nodo
     */
    public void cuelga (NodoAVL<T> hijo, boolean ladoDer){
        if (ladoDer)
            der = hijo;
        else
            izq = hijo;
        if (hijo != null)
            hijo.papa = this;
    }
     /**
     * Método que cuelga un nodo debajo
     * @param hijo Nodo a colgar
     */
    public void cuelga(NodoAVL<T> hijo){
        if(hijo == null)
            return;
        if(hijo.getElem().compareTo(elem)<=0){
            izq = hijo;
        }else{
            der = hijo;
        }
        hijo.papa = this;
    }

}
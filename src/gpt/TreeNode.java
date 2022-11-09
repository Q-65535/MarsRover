package gpt;

import java.util.*;

public abstract class TreeNode {
   private String name;
   protected TreeNode parent;
   protected String Name;
   protected TreeNode next;

   public TreeNode(String name) {
       this.name = name;
   }

   public String getName(){
       return this.name;
   }

   public TreeNode getParent() {
       return this.parent;
   }

   public void setParent(TreeNode parent) {
       this.parent = parent;
   }

   public TreeNode getNext() {
       return this.next;
   }

   public void setNext(TreeNode next) {
       this.next = next;
   }

   @Override
   public String toString() {
       return this.name;
   }
}

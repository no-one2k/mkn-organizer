/*  
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package test.andro;

/**
 *
 * @author noone
 */
public enum TaskPriority {
    VeryImportant,
    Important,
    NotImportant;
    
    public static TaskPriority getByOrdinal (int ord){
        if (VeryImportant.ordinal()==ord){
            return VeryImportant;
        }else{
            if (Important.ordinal()==ord){
                return Important;
            }else{
                return NotImportant;
            }
        }
    }
}

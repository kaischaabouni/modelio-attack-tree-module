package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.DiagramElementBounds;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.module.attacktreedesigner.utils.TagsManager;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("1bdbb2e0-7f1a-46d7-ab20-04f90652f854")
public class AttackTreeDiagramCommand extends DefaultModuleCommandHandler {
    @objid ("5f6ab922-1258-4f74-a63b-49ff3e9b6988")
    private static final String DIAGRAM_DEFAULT_NAME = "Diagram";

    @objid ("58a5f190-c903-4ec2-aea0-41aaddb30e74")
    private static final String TREE = "Tree";

    @objid ("dfab0bf7-9d32-4d00-88ff-2a1653f7b0ee")
    private static final String ATTACKTREE_STYLE_NAME = "attacktree";

    @objid ("289b34c8-b854-4b43-ba33-6c907c5e23b7")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        ModelElement owner = (ModelElement) selectedElements.get(0);    
        
        Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Name", owner.getName());
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.Create", "AttackTree Diagram"))){
        
            // create Root Class
            ModelElement rootElement = session.getModel().createClass(Labels.DEFAULT_NAME.toString(), (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT);
        
            // create Default tags
            TagsManager.createAttackDefaultTags(session, rootElement);
            
            MClass mclass = moduleContext.getModelioServices().getMetamodelService().getMetamodel().getMClass(ClassDiagram.class);
            Stereotype ster = session.getMetamodelExtensions().getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK_TREE_DIAGRAM, mclass);
            ClassDiagram diagram = session.getModel().createClassDiagram(Labels.DEFAULT_NAME.toString(), rootElement, ster);
        
        
            /*
             * Unmask default Root node in the diagram
             */
            IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                IDiagramDG dg = diagramHandle.getDiagramNode();
        
                for (IStyleHandle style : diagramService.listStyles()){
                    if (style.getName().equals(ATTACKTREE_STYLE_NAME)){
                        dg.setStyle(style);
                        break;
                    }
                }
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.unmask(rootElement, 0, 0);
                for (IDiagramGraphic diagramGraphic : diagramGraphics) {
                    if(diagramGraphic.getElement().equals(rootElement)){
                        ((IDiagramNode) diagramGraphic).setBounds(DiagramElementBounds.ROOT.createRectangleBounds());
                    }
                }
                diagramHandle.save();
                diagramHandle.close();
            }
        
            session.getModel().getDefaultNameService().setDefaultName(rootElement, TREE);
            session.getModel().getDefaultNameService().setDefaultName(diagram, rootElement.getName() + " " + DIAGRAM_DEFAULT_NAME);
            moduleContext.getModelioServices().getEditionService().openEditor(diagram);
            
            transaction.commit ();
        }
    }

    @objid ("2375ec67-7c56-4d00-a193-52f2683549a1")
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElt = selectedElements.get(0);
            return ((selectedElt != null) &&
                    (((selectedElt instanceof Package) 
                            && !(selectedElt instanceof Profile)
                            && selectedElt.getStatus().isModifiable())));
        }
        return false;
    }

}

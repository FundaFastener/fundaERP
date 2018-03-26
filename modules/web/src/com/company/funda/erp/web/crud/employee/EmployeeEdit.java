package com.company.funda.erp.web.crud.employee;

import java.util.Map;

import com.company.funda.erp.entity.Employee;
import com.haulmont.cuba.gui.components.AbstractEditor;

public class EmployeeEdit extends AbstractEditor<Employee> {
	
//	private Logger logger = LoggerFactory.getLogger(EmployeeEdit.class);
//	
//	
//	@Inject
//	private ManagerChangeDepartmentValidator managerChangeDepartmentValidator;
	
	@Override
    public void init(Map<String, Object> params) {
//		Field field = (Field)getComponent("fieldGroup.firstNameCh");
//		field.requestFocus();
//		field.setStyleName(HaloTheme.TEXTFIELD_BORDERLESS);
		super.init(params);
		//TODO-H here should verify this man is manager and return alert warn.
//		Field departmentField = (Field)getComponent("fieldGroup.department");
//		
//		departmentField.addValidator(v->{
//			Employee newStateEmployee = getItem();
//			HashMap<String, String> errorMsg = new HashMap<>();
//			errorMsg = managerChangeDepartmentValidator.validate(newStateEmployee, errorMsg);
//			logger.debug("action ------------ v");
//			if(!errorMsg.isEmpty()) {
//				logger.debug("======== {} =========",errorMsg.get("ManagerChangeDepartmentValidator"));
//				throw new ValidationException(errorMsg.get("ManagerChangeDepartmentValidator"));
//			}
//		});
    }

//	@Override
//	protected boolean preCommit() {
//		Employee newStateEmployee = getItem();
//		HashMap<String, String> errorMsg = new HashMap<>();
//		logger.debug("preCommit -----------\n{} ",PrintUtil.printMultiLine(newStateEmployee));
//		errorMsg = managerChangeDepartmentValidator.validate(newStateEmployee, errorMsg);
//		if(!errorMsg.isEmpty()) {
//			showNotification(errorMsg.get("ManagerChangeDepartmentValidator"), NotificationType.WARNING);
//			logger.debug("1111 : {}",getComponent("fieldGroup.department"));
//			Field departmentField = (Field)getComponent("fieldGroup.department");
//			departmentField.requestFocus();
//			return false;
//		}
//		return super.preCommit();
//	}
	
	
	
}
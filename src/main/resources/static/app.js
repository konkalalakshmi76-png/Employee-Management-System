const API_BASE = '/api';
const AUTH_HEADERS = { Authorization: 'Basic ' + btoa('admin:admin123') };

const employeeForm = document.getElementById('employeeForm');
const departmentForm = document.getElementById('departmentForm');
const employeeList = document.getElementById('employeeList');
const departmentSelect = document.getElementById('departmentId');
const filterDepartment = document.getElementById('filterDepartment');
let employees = [];

async function request(path, options = {}) {
    const response = await fetch(`${API_BASE}${path}`, { ...options, headers: { ...AUTH_HEADERS, ...options.headers } });
    const data = await response.json().catch(() => ({}));
    if (!response.ok) throw new Error(data.message || data.error || `Request failed (${response.status})`);
    return data;
}

function showNotice(message, isError = false) {
    const notice = document.getElementById('notice');
    notice.textContent = message;
    notice.className = `notice visible ${isError ? 'error' : 'success'}`;
    window.setTimeout(() => notice.classList.remove('visible'), 3500);
}

async function loadDepartments() {
    const data = await request('/departments');
    const departments = data.data || [];
    document.getElementById('departmentCount').textContent = departments.length;

    departmentSelect.innerHTML = '<option value="">Select Department</option>';
    filterDepartment.innerHTML = '<option value="">All departments</option>';

    departments.forEach(department => {
        const option = document.createElement('option');
        option.value = department.id;
        option.textContent = department.departmentName;
        departmentSelect.appendChild(option.cloneNode(true));
        filterDepartment.appendChild(option);
    });
    document.getElementById('departmentList').innerHTML = departments.map(department => `<div class="department-item"><div><strong>${escapeHtml(department.departmentName)}</strong><span>${escapeHtml(department.location)}</span></div><button class="icon-button" title="Edit department" data-department-edit="${department.id}">Edit</button></div>`).join('');
    document.querySelectorAll('[data-department-edit]').forEach(button => button.addEventListener('click', () => editDepartment(departments.find(item => item.id == button.dataset.departmentEdit))));
}

async function loadEmployees() {
    const params = new URLSearchParams();
    params.append('page', '0');
    params.append('size', '20');

    const name = document.getElementById('searchName').value.trim();
    const designation = document.getElementById('filterDesignation').value.trim();
    const deptId = filterDepartment.value;

    if (name) params.append('name', name);
    if (designation) params.append('designation', designation);
    if (deptId) params.append('department', deptId);

    const data = await request(`/employees?${params.toString()}`);
    employees = data.data || [];
    document.getElementById('employeeCount').textContent = data.totalElements ?? employees.length;

    if (!employees.length) {
        employeeList.innerHTML = '<div class="empty-state"><strong>No employees found</strong><span>Try changing the filters or add a new directory record.</span></div>';
        return;
    }

    employeeList.innerHTML = `
        <table>
            <thead>
                <tr>
                    <th>Code</th>
                    <th>Name</th>
                    <th>Designation</th>
                    <th>Department</th>
                    <th>Salary</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                ${employees.map(employee => `
                    <tr>
                        <td><strong>${escapeHtml(employee.employeeCode)}</strong></td>
                        <td><strong>${escapeHtml(employee.firstName)} ${escapeHtml(employee.lastName)}</strong><small>${escapeHtml(employee.email)}</small></td>
                        <td>${escapeHtml(employee.designation)}</td>
                        <td>${escapeHtml(employee.departmentName || 'N/A')}</td>
                        <td>${Number(employee.salary).toLocaleString(undefined, { style: 'currency', currency: 'INR', maximumFractionDigits: 0 })}</td>
                        <td><span class="status ${employee.status.toLowerCase()}">${escapeHtml(employee.status.replace('_', ' '))}</span></td>
                        <td>
                            <button class="table-action" data-id="${employee.id}" data-action="edit">Edit</button><button class="table-action danger" data-id="${employee.id}" data-action="delete">Delete</button>
                        </td>
                    </tr>
                `).join('')}
            </tbody>
        </table>
    `;

    document.querySelectorAll('[data-action="delete"]').forEach(btn => {
        btn.addEventListener('click', async () => {
            const id = btn.dataset.id;
            const employee = employees.find(item => item.id == id);
            if (btn.dataset.action === 'edit') return populateEmployeeForm(employee);
            if (!window.confirm(`Delete ${employee.firstName} ${employee.lastName}?`)) return;
            try { await request(`/employees/${id}`, { method: 'DELETE' }); showNotice('Employee deleted'); await loadEmployees(); } catch (error) { showNotice(error.message, true); }
        });
    });
}

employeeForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const payload = {
        employeeCode: document.getElementById('employeeCode').value,
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        designation: document.getElementById('designation').value,
        salary: Number(document.getElementById('salary').value),
        joiningDate: document.getElementById('joiningDate').value,
        status: document.getElementById('status').value,
        departmentId: Number(document.getElementById('departmentId').value),
        address: {
            street: document.getElementById('street').value,
            city: document.getElementById('city').value,
            state: document.getElementById('state').value,
            pincode: document.getElementById('pincode').value,
            country: document.getElementById('country').value
        }
    };

    const editId = employeeForm.dataset.editId;
    try {
        await request(editId ? `/employees/${editId}` : '/employees', {
            method: editId ? 'PUT' : 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...AUTH_HEADERS
        },
        body: JSON.stringify(payload)
        });
        resetEmployeeForm(); showNotice(editId ? 'Employee updated' : 'Employee added'); await loadEmployees();
    } catch (error) { showNotice(error.message, true); }
});

departmentForm.addEventListener('submit', async (event) => {
    event.preventDefault();

    const payload = {
        departmentName: document.getElementById('departmentName').value,
        description: document.getElementById('departmentDescription').value,
        location: document.getElementById('departmentLocation').value
    };

    const editId = document.getElementById('departmentEditId').value;
    try { await request(editId ? `/departments/${editId}` : '/departments', {
        method: editId ? 'PUT' : 'POST',
        headers: {
            'Content-Type': 'application/json',
            ...AUTH_HEADERS
        },
        body: JSON.stringify(payload)
    }); resetDepartmentForm(); showNotice(editId ? 'Department updated' : 'Department added'); await loadDepartments(); await loadEmployees();
    } catch (error) { showNotice(error.message, true); }
});

document.getElementById('applyFilters').addEventListener('click', loadEmployees);
document.getElementById('clearFilters').addEventListener('click', () => { document.getElementById('searchName').value = ''; filterDepartment.value = ''; document.getElementById('filterDesignation').value = ''; loadEmployees(); });
document.getElementById('resetEmployeeForm').addEventListener('click', resetEmployeeForm);
document.getElementById('resetDepartmentForm').addEventListener('click', resetDepartmentForm);

function resetEmployeeForm() { employeeForm.reset(); delete employeeForm.dataset.editId; document.getElementById('employeeFormTitle').textContent = 'Add employee'; document.getElementById('employeeSubmit').textContent = 'Save employee'; document.getElementById('editBadge').classList.add('hidden'); }
function resetDepartmentForm() { departmentForm.reset(); document.getElementById('departmentEditId').value = ''; document.getElementById('departmentSubmit').textContent = 'Add department'; }
function populateEmployeeForm(employee) { Object.entries(employee).forEach(([key, value]) => { const input = document.getElementById(key); if (input && value != null && typeof value !== 'object') input.value = value; }); employeeForm.dataset.editId = employee.id; document.getElementById('departmentId').value = employee.departmentId; Object.entries(employee.address || {}).forEach(([key, value]) => { const input = document.getElementById(key); if (input && value != null) input.value = value; }); document.getElementById('employeeFormTitle').textContent = 'Edit employee'; document.getElementById('employeeSubmit').textContent = 'Update employee'; document.getElementById('editBadge').classList.remove('hidden'); window.scrollTo({ top: 0, behavior: 'smooth' }); }
function editDepartment(department) { document.getElementById('departmentEditId').value = department.id; document.getElementById('departmentName').value = department.departmentName; document.getElementById('departmentLocation').value = department.location; document.getElementById('departmentDescription').value = department.description || ''; document.getElementById('departmentSubmit').textContent = 'Update department'; }
function escapeHtml(value) { return String(value ?? '').replace(/[&<>'"]/g, character => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', "'": '&#39;', '"': '&quot;' }[character])); }

loadDepartments();
loadEmployees();

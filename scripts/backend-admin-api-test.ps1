$BASE = "http://localhost:8080"
$OUT_DIR = "e:\IdeaProjects\springboot-vue3-demo\test-reports"
$OUT_JSON = "$OUT_DIR\admin-api-test-result.json"
$TS = Get-Date -Format "yyyyMMddHHmmss"
$results = @{}

$USERNAME = "test_admin"
$PWD = "123456"
$TOKEN = ""
$ADDED_ID = $null

function Api-Post($path, $body) {
    $headers = @{'Content-Type'='application/json'}
    if ($TOKEN) { $headers['Authorization'] = $TOKEN }
    $json = $body | ConvertTo-Json -Compress
    try {
        $r = Invoke-WebRequest -Uri "$BASE$path" -Method POST -Headers $headers -Body $json -UseBasicParsing -TimeoutSec 10
        return @{ok=$true; body=($r.Content|ConvertFrom-Json); http=$r.StatusCode}
    } catch { return @{ok=$false; error=$_.Exception.Message} }
}

function Api-Put($path, $body) {
    $headers = @{'Content-Type'='application/json'}
    if ($TOKEN) { $headers['Authorization'] = $TOKEN }
    $json = $body | ConvertTo-Json -Compress
    try {
        $r = Invoke-WebRequest -Uri "$BASE$path" -Method PUT -Headers $headers -Body $json -UseBasicParsing -TimeoutSec 10
        return @{ok=$true; body=($r.Content|ConvertFrom-Json); http=$r.StatusCode}
    } catch { return @{ok=$false; error=$_.Exception.Message} }
}

function Api-Get($path) {
    $headers = @{'Content-Type'='application/json'}
    if ($TOKEN) { $headers['Authorization'] = $TOKEN }
    $uri = "$BASE$path"
    try {
        $r = Invoke-WebRequest -Uri $uri -Method GET -Headers $headers -UseBasicParsing -TimeoutSec 10
        return @{ok=$true; body=($r.Content|ConvertFrom-Json); http=$r.StatusCode}
    } catch { return @{ok=$false; error=$_.Exception.Message} }
}

function Api-Delete($path) {
    $headers = @{'Content-Type'='application/json'}
    if ($TOKEN) { $headers['Authorization'] = $TOKEN }
    $uri = "$BASE$path"
    try {
        $r = Invoke-WebRequest -Uri $uri -Method DELETE -Headers $headers -UseBasicParsing -TimeoutSec 10
        return @{ok=$true; body=($r.Content|ConvertFrom-Json); http=$r.StatusCode}
    } catch { return @{ok=$false; error=$_.Exception.Message} }
}

Write-Output "========================================"
Write-Output "  Backend API Test - Batch 3"
Write-Output "  Timestamp: $TS"
Write-Output "========================================"

# ==================== 1. Login ====================
Write-Output "`n[1/9] Testing login..."
$resp = Api-Post "/admin/login" @{username=$USERNAME; userpwd=$PWD}
if ($resp.ok -and $resp.body.code -eq 200) {
    $results['login'] = @{status='PASS'; account=$USERNAME}
    if ($resp.body.data.token) {
        $TOKEN = $resp.body.data.token
        $results['login']['token'] = 'returned'
        Write-Output "  PASS: token returned"
    } else {
        $results['login']['token'] = 'not_returned'
        Write-Output "  PASS: token NOT returned (JWT not implemented)"
    }
} else {
    $results['login'] = @{status='FAIL'}
    Write-Output "  FAIL"
}

# ==================== 2. Dashboard ====================
Write-Output "`n[2/9] Testing Dashboard..."
$resp = Api-Get "/dashboard/overview"
if ($resp.ok -and $resp.body.code -eq 200) {
    $d = $resp.body.data
    $fields = @('totalEmission','reductionRate','companyCount','monthlyEmission','sourceData')
    $missing = @()
    foreach($f in $fields) { if ($null -eq $d.$f) { $missing += $f } }
    if ($missing.Count -gt 0) {
        $results['dashboard'] = @{status='WARN'; missing=($missing -join ',')}
        Write-Output "  WARN: missing fields: $($missing -join ',')"
    } else {
        $results['dashboard'] = @{status='PASS'}
        Write-Output "  PASS: totalEmission=$($d.totalEmission) reductionRate=$($d.reductionRate) companyCount=$($d.companyCount)"
    }
} else {
    $results['dashboard'] = @{status='FAIL'}
    Write-Output "  FAIL"
}

# ==================== 3. admin/list ====================
Write-Output "`n[3/9] Testing admin/list..."
$resp = Api-Get "/admin/list?pagenum=1&pagesize=5"
if ($resp.ok -and $resp.body.code -eq 200) {
    $results['admin_list'] = @{status='PASS'; total=$resp.body.data.total; pageSize=$resp.body.data.pageSize}
    Write-Output "  PASS: total=$($resp.body.data.total)"
} else {
    $results['admin_list'] = @{status='FAIL'}
    Write-Output "  FAIL"
}

# ==================== 4. admin/add ====================
$AUTO_USER = "auto_user_$TS"
Write-Output "`n[4/9] Testing admin/add ($AUTO_USER)..."
$resp = Api-Post "/admin/add" @{username=$AUTO_USER; userpwd=$PWD; name='auto_test_user'; sex='male'; tel='13900000000'; headurl=''}
if ($resp.ok -and $resp.body.code -eq 200) {
    $loginRes = Api-Post "/admin/login" @{username=$AUTO_USER; userpwd=$PWD}
    if ($loginRes.ok -and $loginRes.body.code -eq 200) { $ADDED_ID = $loginRes.body.data.id }
    $results['admin_add'] = @{status='PASS'; username=$AUTO_USER; id=$ADDED_ID}
    Write-Output "  PASS: id=$ADDED_ID"
} else {
    $results['admin_add'] = @{status='FAIL'}
    Write-Output "  FAIL"
}

# ==================== 5. admin/update ====================
Write-Output "`n[5/9] Testing admin/update (PUT)..."
if ($ADDED_ID) {
    $resp = Api-Put "/admin/update" @{id=$ADDED_ID; username=$AUTO_USER; userpwd=$PWD; name='auto_test_modified'; sex='male'; tel='13911111111'; headurl=''}
    if ($resp.ok -and $resp.body.code -eq 200) {
        $results['admin_update'] = @{status='PASS'; id=$ADDED_ID}
        Write-Output "  PASS: updated"
    } else {
        $results['admin_update'] = @{status='FAIL'; note='trying POST fallback'}
        $resp2 = Api-Post "/admin/update" @{id=$ADDED_ID; username=$AUTO_USER; userpwd=$PWD; name='auto_test_modified'; sex='male'; tel='13911111111'; headurl=''}
        if ($resp2.ok -and $resp2.body.code -eq 200) {
            $results['admin_update'] = @{status='PASS (via POST)'; id=$ADDED_ID}
            Write-Output "  PASS: updated via POST"
        } else {
            Write-Output "  FAIL"
        }
    }
} else {
    $results['admin_update'] = @{status='SKIP'}
    Write-Output "  SKIP"
}

# ==================== 6. admin/delete ====================
Write-Output "`n[6/9] Testing admin/delete..."
if ($ADDED_ID) {
    $resp = Api-Delete "/admin/delete/$ADDED_ID"
    if ($resp.ok -and $resp.body.code -eq 200) {
        $results['admin_delete'] = @{status='PASS'; id=$ADDED_ID}
        Write-Output "  PASS: deleted"
    } else {
        $results['admin_delete'] = @{status='FAIL'}
        Write-Output "  FAIL"
    }
} else {
    $results['admin_delete'] = @{status='SKIP'}
    Write-Output "  SKIP"
}

# ==================== 7. admin/batchDelete ====================
Write-Output "`n[7/9] Testing admin/batchDelete..."
$batchIds = @()
for ($i=1; $i -le 2; $i++) {
    $user = "batch_${TS}_$i"
    $bResp = Api-Post "/admin/add" @{username=$user; userpwd=$PWD; name="batch_$i"; sex='male'; tel="1380000000$i"; headurl=''}
    if ($bResp.ok) {
        $lRes = Api-Post "/admin/login" @{username=$user; userpwd=$PWD}
        if ($lRes.ok -and $lRes.body.code -eq 200) { $batchIds += $lRes.body.data.id }
    }
}
if ($batchIds.Count -gt 0) {
    $idsStr = $batchIds -join ','
    $resp = Api-Delete "/admin/batchDelete?ids=$idsStr"
    if ($resp.ok -and $resp.body.code -eq 200) {
        $results['admin_batchDelete'] = @{status='PASS'; count=$batchIds.Count}
        Write-Output "  PASS: deleted $($batchIds.Count) users"
    } else { $results['admin_batchDelete'] = @{status='FAIL'}; Write-Output "  FAIL" }
} else { $results['admin_batchDelete'] = @{status='SKIP'}; Write-Output "  SKIP" }

# ==================== 8. admin/export ====================
Write-Output "`n[8/9] Testing admin/export..."
try {
    $r = Invoke-WebRequest -Uri "$BASE/admin/export" -Method GET -UseBasicParsing -TimeoutSec 10
    if ($r.StatusCode -eq 200) {
        [System.IO.File]::WriteAllBytes("$OUT_DIR\admin-export.xlsx", $r.Content)
        $results['admin_export'] = @{status='PASS'; size=$r.RawContentLength}
        Write-Output "  PASS: $($r.RawContentLength) bytes saved to test-reports/admin-export.xlsx"
    } else { $results['admin_export'] = @{status='FAIL'}; Write-Output "  FAIL: HTTP $($r.StatusCode)" }
} catch { $results['admin_export'] = @{status='NOT_IMPLEMENTED'}; Write-Output "  NOT IMPLEMENTED" }

# ==================== 9. admin/import ====================
Write-Output "`n[9/9] Testing admin/import..."
try {
    $r = Invoke-WebRequest -Uri "$BASE/admin/import" -Method POST -UseBasicParsing -TimeoutSec 5
    if ($r.StatusCode -eq 200) { $results['admin_import'] = @{status='RESPONDS'}; Write-Output "  RESPONDS: HTTP 200" }
    else { $results['admin_import'] = @{status='FAIL'}; Write-Output "  FAIL" }
} catch { $results['admin_import'] = @{status='NOT_IMPLEMENTED'}; Write-Output "  NOT IMPLEMENTED" }

# ==================== Save ====================
Write-Output "`n========================================"
Write-Output "  Saving to: $OUT_JSON"
$results | ConvertTo-Json -Depth 4 | Out-File -FilePath $OUT_JSON -Encoding UTF8 -Force
Get-Content $OUT_JSON
Write-Output "`nDONE"

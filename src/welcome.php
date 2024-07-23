<?php
function containsXSS($input) {
    $pattern = '/<.*?>/';
    return preg_match($pattern, $input);
}

function containsSQLInjection($input) {
    // List of common SQL Injection patterns
    $patterns = [
        "/(\%27)|(\')|(\-\-)|(\%23)|(#)/i",
        "/((\%3D)|(=))[^\n]*((\%27)|(\')|(\-\-)|(\%3B)|(;))/i",
        "/\w*((\%27)|(\'))(\s)*(or|OR|and|AND)(\s)*((\%27)|(\'))(\w|\s)*/i",
        "/exec(\s|\+)+(s|x)p\w+/i",
        "/union(\s|\+)+select/i",
        "/select(\s|\+)+.*from/i",
        "/insert(\s|\+)+into(\s|\+)+.*values/i",
        "/update(\s|\+)+.*set/i",
        "/delete(\s|\+)+from/i"
    ];

    foreach ($patterns as $pattern) {
        if (preg_match($pattern, $input)) {
            return true;
        }
    }
    return false;
}

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $query = $_POST['query'];
    if (containsXSS($query) || containsSQLInjection($query)) {
        header("Location: index.php");
        exit();
    } else {
        $query = htmlspecialchars($query, ENT_QUOTES, 'UTF-8');
    }
} else {
    header("Location: index.php");
    exit();
}
?>

<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
</head>
<body>
    <h2>Welcome</h2>
    <p>Your search query is: <?php echo $query; ?></p>
    <form method="POST" action="index.php">
        <button type="submit">Return</button>
    </form>
</body>
</html>

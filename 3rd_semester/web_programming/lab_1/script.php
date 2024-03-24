<?php
    function is_in_area($X, $Y, $R) {
        $in_area = false;

        if ($X > 0) {
            if ($Y > 0) {
                $in_area = false;
            } else {
                if ((-1 * $Y <= $R / 2) and $X <= $R) {
                    $in_area = true;
                } else {
                    $in_area = false;
                }
            }
        } else {
            if ($Y > 0) {
                if ($X * $X + $Y * $Y <= $R * $R / 4) {
                    $in_area = true;
                }
            } else {
                if ($Y >= -1 * ($X + $R) / 2) {
                    $in_area = true;
                } else {
                    $in_area = false;
                }
            }
        }

        return $in_area;
    }

    function is_valid($X_str, $Y_str, $R_str) {
        $x = round((float)$X_str, 4);
        $y = round((float)$Y_str, 4);
        $r = round((float)$R_str, 4);
        
        if ((string)$x == $X_str and (string)$y == $Y_str and (string)$r == $R_str and $r >= 0) {
            if ($x >= -3 and $x <= 5 and $y >= -3 and $y <= 5 and $r <= 5) {
                return -1;
            } else {
                return 1;
            } 
        } else {
            if ((float)$X_str - $x != 0 or (float)$Y_str - $y != 0 or (float)$R_str - $r != 0) {
                return 2;
            }
            return 0;
        }
    }

    function to_float($param) {
        if ($param != null) {
            return (float)$param;
        }
        return null;
    }

    date_default_timezone_set('Europe/Moscow');
    $current_datetime = date('d-m-Y H:i:s');
    $start_time = microtime(true);

    $X_str = isset($_GET['X']) ? $_GET['X'] : null;
    $Y_str = isset($_GET['Y']) ? $_GET['Y'] : null;
    $R_str = isset($_GET['R']) ? $_GET['R'] : null;
    $mode = isset($_GET['mode']) ? $_GET['mode'] : "empty";

    $X = to_float($X_str);
    $Y = to_float($Y_str);
    $R = to_float($R_str);

    if ($mode == "start") {
        $cachefile = "./cache/php_cache.cache";

        if (file_exists($cachefile)) {
            $file_content = file_get_contents($cachefile);
            echo $file_content;
        } else {
            $fp = fopen($cachefile, "w");
            fclose($fp);
        }
        exit();

    } else if ($mode == "empty") {
        echo "Empty mode.";
        exit();

    } else if (is_float($X) and is_float($Y) and is_float($R)) {
        $valid_code = is_valid($X_str, $Y_str, $R_str);

        if ($valid_code == -1) {
            $in_area = is_in_area($X, $Y, $R);    

            $end_time = microtime(true);
            $execution_time = round(($end_time - $start_time) * 1000000, 4);

            $in_area_str = $in_area ? "Yes" : "No";

            $cachefile = "./cache/php_cache.cache";

            if (file_exists($cachefile)) {
                $file_content = file_get_contents($cachefile);
                echo $file_content;
            }
            ob_start();
            
            echo "<tr><td>" . $current_datetime . "</td><td>" . $execution_time . "</td><td>" . $X . "</td><td>" . $Y . "</td><td>" . $R . "</td><td>" . $in_area_str . "</td></tr>\n";
            
            $file_content = isset($file_content) ? $file_content . ob_get_contents() : ob_get_contents();
            ob_end_flush();

            if (file_exists($cachefile)) {
                file_put_contents($cachefile, $file_content);
            } else {
                $fp = fopen($cachefile, "w");
                fwrite($fp, $file_content);
                fclose($fp);
            }

        } else if ($valid_code == 0) {
            echo "Enter a number, not a string.";
        } else if ($valid_code == 1) {
            echo "Some of the entered values are out of tolerance.";
        } else {
            echo "The number of digits in the decimal part of all values shall not exceed 4.";
        }
    } else {
        echo "Enter all values.";
    }
?>
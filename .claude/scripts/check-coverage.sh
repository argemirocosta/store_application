#!/usr/bin/env bash
# Verifica se os arquivos .java novos/alterados em src/main (em relação a uma branch
# base) têm cobertura de linha mínima, segundo um relatório JaCoCo (jacoco.csv).
#
# Uso: check-coverage.sh <jacoco.csv> <limite-percentual> [branch-base]
#
# Saída: uma linha por arquivo (OK/FALHA/SKIP) e código de saída != 0 se algum
# arquivo ficar abaixo do limite.

set -euo pipefail

CSV="${1:?uso: check-coverage.sh <jacoco.csv> <limite-percentual> [branch-base]}"
THRESHOLD="${2:?uso: check-coverage.sh <jacoco.csv> <limite-percentual> [branch-base]}"
BASE="${3:-main}"

if [ ! -f "$CSV" ]; then
	echo "Relatório JaCoCo não encontrado: $CSV" >&2
	exit 2
fi

FILES=$(git diff --name-only --diff-filter=ACMR "${BASE}...HEAD" -- '*.java' | grep '^src/main/' || true)

if [ -z "$FILES" ]; then
	echo "Nenhum arquivo de produção (src/main) novo ou alterado em relação a '${BASE}'."
	exit 0
fi

FAIL=0
while IFS= read -r file; do
	[ -z "$file" ] && continue

	rel="${file#src/main/}"
	pkg=$(dirname "$rel" | tr '/' '.')
	cls=$(basename "$rel" .java)

	# Soma LINE_MISSED (col 8) e LINE_COVERED (col 9) de todas as classes do arquivo,
	# incluindo inner classes (Foo$Builder, Foo$1, etc.)
	read -r missed covered <<<"$(awk -F, -v pkg="$pkg" -v cls="$cls" '
		NR > 1 && $2 == pkg && ($3 == cls || index($3, cls "$") == 1) {
			m += $8; c += $9
		}
		END { print m + 0, c + 0 }
	' "$CSV")"

	total=$((missed + covered))
	if [ "$total" -eq 0 ]; then
		echo "SKIP  $file (sem linhas executáveis no relatório)"
		continue
	fi

	pct=$((covered * 100 / total))
	if [ "$pct" -lt "$THRESHOLD" ]; then
		echo "FALHA $file - cobertura de linha: ${pct}% (mínimo ${THRESHOLD}%)"
		FAIL=1
	else
		echo "OK    $file - cobertura de linha: ${pct}%"
	fi
done <<<"$FILES"

exit $FAIL
